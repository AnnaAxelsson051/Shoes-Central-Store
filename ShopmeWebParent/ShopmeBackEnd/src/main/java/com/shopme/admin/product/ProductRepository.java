package com.shopme.admin.product;

import org.springframework.data.repository.CrudRepository;
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
}
