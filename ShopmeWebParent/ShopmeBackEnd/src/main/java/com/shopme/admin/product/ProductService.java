package com.shopme.admin.product;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;

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
	
	//Sorts products accoring to keyword displaying 5 per page
	public Page <Product> listByPage(int pageNum, String sortField, 
			String sortDir, String keyword, Integer categoryId){
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
	Pageable pageable = (Pageable) PageRequest.of(pageNum - 1,  PRODUCTS_PER_PAGE, sort);
	
	
	if(keyword != null && !keyword.isEmpty()) {
		if(categoryId != null && categoryId > 0) {
			String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
		return repo.findAllInCategory(categoryId, categoryIdMatch, pageable);
		}
		return repo.findAll(keyword,  pageable);
		}
	
	if(categoryId != null && categoryId > 0) {
		String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
	return repo.findAllInCategory(categoryId, categoryIdMatch, pageable);
	}
	return repo.findAll(pageable);
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
