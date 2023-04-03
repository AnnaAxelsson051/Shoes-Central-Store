package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;

import java.awt.print.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

//public interface BrandRepository extends PagingAndSortingRepository <Brand, Integer>{
	public interface BrandRepository extends CrudRepository <Brand, Integer>{

		
		public Long countById(Integer id);
		
		Brand findByName(String name);
		
		//For search for brands for name column
		@Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
		public Page<Brand> findAll(String keyword, Pageable pageable);
}
