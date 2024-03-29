package com.shopme.admin.product;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class ProductService {
	public static final int PRODUCTS_PER_PAGE = 5;
	
	@Autowired
	private ProductRepository repo;
	
	public List <Product> listAll(){
		return (List<Product>) repo.findAll();
	}
	
	//Sorts products and subcategories accoring to keyword displaying 5 per page
	public void listByPage(int pageNum, PagingAndSortingHelper helper, Integer categoryId){
	//Pageable pageable = (Pageable) PageRequest.of(pageNum - 1,  PRODUCTS_PER_PAGE, sort);
	Pageable pageable = helper.createPageable(PRODUCTS_PER_PAGE, pageNum);
	String keyword = helper.getKeyword();
	Page<Product> page = null;
	
	
	if(keyword != null && !keyword.isEmpty()) {
		if(categoryId != null && categoryId > 0) {
			String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
		page = repo.findAllInCategory(categoryId, categoryIdMatch, pageable);
		} else {
		page = repo.findAll(keyword, pageable);
		}
	}else {
		if(categoryId != null && categoryId > 0) {
			String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
		page = repo.findAllInCategory(categoryId, categoryIdMatch, pageable);
	} else {
		page = repo.findAll(pageable);
	}
	}
		helper.updateModelAttributes(pageNum, page);
	}
	
	//Similar to listByPage searching products
	//creating a pageable objectcalling the repo search method passing keyword and pageable obj
	//calling helper to update the model with info
	public void searchProducts(int pageNum, PagingAndSortingHelper helper){
	Pageable pageable = helper.createPageable(PRODUCTS_PER_PAGE,  pageNum);
	String keyword = helper.getKeyword();
	
	Page<Product> page = repo.searchProductsByName(keyword,pageable);
	
	helper.updateModelAttributes(pageNum, page);
	}

	
	//for saving a product when creating one
	//saving alias as productname where white spaces 
	//replaced by dashes
public Product save(Product product) {
	if(product.getId() == null) {
		product.setCreatedTime(new Date());
	}
	
	if(product.getAlias() == null || product.getAlias().isEmpty()) {
		String defaultAlias = product.getName().replaceAll(" ", "-");
	product.setAlias(defaultAlias);
	}else {
		product.setAlias(product.getAlias().replaceAll(" ", "-"));
	}
	product.setUpdatedTime(new Date());
	
	return repo.save(product);
}

//Saves price information of product
public void saveProductPrice(Product productInForm) {
	Product productInDB = repo.findById(productInForm.getId()).get();
productInDB.setCost(productInForm.getCost());
productInDB.setPrice(productInForm.getPrice());
productInDB.setDiscountPercent(productInForm.getDiscountPercent());

repo.save(productInDB);
}


//Checks if productname is unique when user is creating a product 
public String CheckUnique(Integer id, String name) {
	boolean isCreatingNew = (id == null|| id == 0);
	Product productByName = repo.findByName(name);
	
	if(isCreatingNew) {
		if(productByName != null) return "Duplicate";
	} else {
		if (productByName != null && productByName.getId() != id) {
			return "Duplicate";
		}
	}
	return "OK";
}

//Updates the status enabled/disabled on products
public void updateProductEnabledStatus(Integer id, boolean enabled) {
	repo.updateEnabledStatus(id,  enabled);
}

//Deletes a product
public void delete(Integer id) throws ProductNotFoundException{
	Long countById = repo.countById(id);
	
	if (countById == null || countById == 0) {
		throw new ProductNotFoundException("Could not find any product with ID " + id);
		
	}
	repo.deleteById(id);
}

//Returns product object based on id
public Product get (Integer id) throws ProductNotFoundException {
	try {
		return repo.findById(id).get();
	} catch (NoSuchElementException ex) {
		throw new ProductNotFoundException("Could not find any product with ID " + id);
	}
}

}
