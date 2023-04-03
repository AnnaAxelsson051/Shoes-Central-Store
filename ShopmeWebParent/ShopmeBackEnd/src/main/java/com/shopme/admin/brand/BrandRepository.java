package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

//public interface BrandRepository extends PagingAndSortingRepository <Brand, Integer>{
	public interface BrandRepository extends CrudRepository <Brand, Integer>{

		
		public Long countById(Integer id);
}
