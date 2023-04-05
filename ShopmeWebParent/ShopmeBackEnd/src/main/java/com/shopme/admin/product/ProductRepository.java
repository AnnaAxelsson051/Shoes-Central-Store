package com.shopme.admin.product;

import org.springframework.data.repository.CrudRepository;

import java.awt.print.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.shopme.common.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Integer>{
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
	
}
