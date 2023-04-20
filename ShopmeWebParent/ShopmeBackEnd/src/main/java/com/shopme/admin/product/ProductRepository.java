package com.shopme.admin.product;

import org.springframework.data.repository.CrudRepository;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.product.Product;

public interface ProductRepository extends CrudRepository <Product, Integer>, PagingAndSortingRepository <Product, Integer>{
	//public interface ProductRepository extends PagingAndSortingRepository <Product, Integer>{

	public Product findByName(String name);
	
	//Sets p enabled to the value of second parameter 
	//where product id = first parameter
	@Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
	@Modifying //For update Query
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	public Long countById(Integer id);
	
	//For sorting products according to specified keyword (param 1)
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%"
			+ "OR p.shortDescription LIKE %?1%"
			+ "OR p.fullDescription LIKE %?1%"
			+ "OR p..brand.name LIKE %?1%"
			+ "OR p.category.name LIKE %?1%")
	public Page<Product> findAll(String keyword, Pageable pageable);
	
	//Filters by categories and category parents without keyword
	@Query("SELECT p FROM Product p WHERE p.category.id = ?1 "
			+ "OR p.category.allParentIDs LIKE %?2%")
	public Page<Product> findAllInCategory(Integer categoryId, 
			String categoryIdMatch, Pageable pageable);

	//Filters by categories and sub categories with keyword
	@Query("SELECT p FROM Product p WHERE "
			+ "(p.category.id = ?1 "
			+ "OR p.category.allParentIDs LIKE %?2%) "
			+ "AND"
			+ "(p.name LIKE %?3%"
			+ "OR p.shortDescription LIKE %?3%"
			+ "OR p.fullDescription LIKE %?3%"
			+ "OR p..brand.name LIKE %?3%"
			+ "OR p.category.name LIKE %?3%)")
public Page<Product> searchInCategory(Integer CategoryId, 
		String categoryIdMatch, String keyword, Pageable pageable);
	
	//Searching for product by keyword returning a page of product objects that
	//can be paginated
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
	public Page<Product> searchProductByName(String keyword, Pageable pageable);
}
