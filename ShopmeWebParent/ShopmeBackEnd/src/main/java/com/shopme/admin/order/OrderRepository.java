package com.shopme.admin.order;

import java.awt.print.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.Product;


	public interface OrderRepository extends CrudRepository <Order, Integer>, PagingAndSortingRepository <Order, Integer>, SearchRepository <Order, Integer>{

		//Finding order
		@Query("SELECT o FROM Order o WHERE CONCAT('#', o.id) LIKE %?1% OR "
				+ " CONCAT(o.firstName, ' ', o.lastName) LIKE %?1% OR"
				+ " o.firstName LIKE %?1% OR"
				+ " o.lastName LIKE %?1% OR o.phoneNumber LIKE %?1% OR"
				+ " o.addressLine1 LIKE %?1% OR o.addressLine2 LIKE %?1% OR"
				+ " o.postalCode LIKE %?1% OR o.city LIKE %?1% OR"
				+ " o.state LIKE %?1% OR o.country LIKE %?1% OR"
				+ " o.paymentMethod LIKE %?1% OR o.status LIKE %?1% OR"
				+ " o.customer.firstName LIKE %?1% OR"
				+ " o.customer.lastName LIKE %?1%")
		public Page<Order> findAll(String keyword, Pageable pageable);
}
