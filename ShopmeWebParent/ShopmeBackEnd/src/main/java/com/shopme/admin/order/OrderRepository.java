package com.shopme.admin.order;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.Product;


	public interface OrderRepository extends CrudRepository <Order, Integer>, PagingAndSortingRepository <Order, Integer>, SearchRepository <Order, Integer>{

}
