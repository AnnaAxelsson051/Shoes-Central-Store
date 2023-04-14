package com.shopme.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository <Product, Integer>, CrudRepository <Product, Integer> {

	//Listing enabled products filtered by product id
	@Query("SELECT p FROM Product p WHERE p.enabled = true "
	+ "AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%)"
	+ "ORDER BY p.name ASC")
	public Page <Product> listByCategory(Integer categoryId, String categoryIDMatch, Pageable pageable);

	public Product findByAlias(String alias);
	
	//Fulltext search
	@Query(value = "SELECT * from products WHERE enabled = TRUE AND"
			+ "MATCH(name,short_description, full_description) AGAINST (?!)",
			nativeQuery = true)
	public Page<Product> search(String keyword, Pageable pageable);
}
