package com.shopme.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import com.shopme.common.entity.Product;

@Service
public class ProductService {
public static final int PRODUCTS_PER_PAGE = 10;

@Autowired private ProductRepository repo;

//Finds category by category id
public Page<Product> listByCategory(int pageNum, Integer categoryId){
	String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
	Pageable pageable = PageRequest.of(pageNum -1, PRODUCTS_PER_PAGE);
	
	return repo.listByCategory(categoryId, categoryIdMatch, pageable);
}
}
