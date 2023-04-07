package com.shopme.category;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Category;

public interface CategoryRepository extends CrudRepository <Category, Integer> {

	//Returns a sorted list of categories that are enabled 
	@Query("SELECT c FROM Category c WHERE c.enabled = true ORDER BY c.name ASC")
	public List<Category> findAllEnabled();
	
}
