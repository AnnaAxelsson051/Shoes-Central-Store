package com.shopme.admin.brand;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.admin.paging.SearchRepository;
//import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.Brand;

//public interface BrandRepository extends SearchRepository <Brand, Integer>{
public interface BrandRepository extends SearchRepository <Brand, Integer>, CrudRepository <Brand, Integer>, PagingAndSortingRepository <Brand, Integer>{

		
		public Long countById(Integer id);
		
		Brand findByName(String name);
		
		//search for brands for name column
		@Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
		public Page<Brand> findAll(String keyword, Pageable pageable);

		//Selecting and ordering a spec brand
		//only selecting brand id and name to show cusomer in form
	@Query("SELECT NEW Brand(b.id, b.name) b FROM Brand b ORDER BY b.name ASC")
	public List<Brand> findAll();
	}
