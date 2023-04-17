package com.shopme.admin.order;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Order;



public class OrderService {
	private static final int ORDERS_PER_PAGE = 10;
	
	@Autowired private OrderRepository repo;
	
	//Listing orders by page
	//If sort field is destination updating the sort object to sort result by
	//country, then by state and city
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		String sortField = helper.getSortField();
		String sortDir = helper.getSortDir();
		String keyword = helper.getKeyword();
		
		Sort sort = null;
		
		if ("destination".equals(sortField)) {
			sort = Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));
		} else {
			sort = Sort.by(sortField);
		}
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);
		
		Page<Order> page = null;
		
		if (keyword != null) {
			page = repo.findAll(keyword, pageable);
		} else {
			page = repo.findAll(pageable);
		}
		
		helper.updateModelAttributes(pageNum, page);		
	}
	
	public Order get(Integer id) throws OrderNotFoundException{
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new OrderNotFoundException("Could not find any order with ID " + id);
		}
		
	}
	
	//deleting an order
	//Counting the number of orders by a given id, if order is found deleting
	public void delete(Integer id) throws OrderNotFoundException {
		Long count = repo.countById(id);
		if (count == null || count == 0) {
			throw new OrderNotFoundException("Could not find any orders with ID " + id); 
		}
		
		repo.deleteById(id);
	}

}
