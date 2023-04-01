package com.shopme.admin.category;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.shopme.common.entity.Category;

//public interface CategoryRepository extends PagingAndSortingRepository <Category, Integer>{
public interface CategoryRepository extends CrudRepository <Category, Integer>{

	//Return a list of category object that do not have parent/top level categories
	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	public List<Category> findRootCategories();
	
	public Category findByName(String name);
	
	public Category findByAlias(String alias);
}





