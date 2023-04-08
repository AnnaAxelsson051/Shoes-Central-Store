package com.shopme.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.common.entity.Product;

@Service
public class ProductService {
public static final int PRODUCTS_PER_PAGE = 10;
public static final int SEARCH_RESULTS_PER_PAGE = 10;

@Autowired private ProductRepository repo;

//Finds products by category id
public Page<Product> listByCategory(int pageNum, Integer categoryId){
	String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
	Pageable pageable = PageRequest.of(pageNum -1, PRODUCTS_PER_PAGE);
	
	return repo.listByCategory(categoryId, categoryIdMatch, pageable);
}

//Finds products by alias
public Product getProduct(String alias) throws ProductNotFoundException {
	Product product = repo.findByAlias(alias);
	if (product == null) {
		throw new ProductNotFoundException("Cound npt find any product with alias " + alias);
	}
	return product;
}

//Fulltext search
public Page<Product> search(String keyword, int pageNum){
	Pageable pageable = PageRequest.of(pageNum -1, SEARCH_RESULTS_PER_PAGE);
return repo.search(keyword,  pageable);
}
}
