package com.shopme.admin.order;

import java.util.List;
import java.util.Date;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.OrderTrack;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderStatus;



public class OrderService {
	private static final int ORDERS_PER_PAGE = 10;
	
	@Autowired private OrderRepository orderRepo;
	@Autowired private CountryRepository countryRepo;
	
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
			page = orderRepo.findAll(keyword, pageable);
		} else {
			page = orderRepo.findAll(pageable);
		}
		
		helper.updateModelAttributes(pageNum, page);		
	}
	
	//Getting an order by its id
	public Order get(Integer id) throws OrderNotFoundException{
		try {
			return orderRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new OrderNotFoundException("Could not find any order with ID " + id);
		}
		
	}
	
	//deleting an order
	//Counting the number of orders by a given id, if order is found deleting
	public void delete(Integer id) throws OrderNotFoundException {
		Long count = orderRepo.countById(id);
		if (count == null || count == 0) {
			throw new OrderNotFoundException("Could not find any orders with ID " + id); 
		}
		
		orderRepo.deleteById(id);
	}

	//Listing all countries in asc order
	public List <Country> listAllCountries(){
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	//Savig order in the form into db 
	//by getting the id of order in form
	public void save(Order orderInForm) {
		Order orderInDb = orderRepo.findById(orderInForm.getId()).get();
		orderInForm.setOrderTime(orderInDb.getOrderTime());
		orderInForm.setCustomer(orderInDb.getCustomer());
	    orderRepo.save(orderInForm);
	}
	
	//Taking the status to be updated, geting an order from db 
	//Converting the string status to an enum constant, checking if the order
	//In db does not have the status to update creatig a new order tracj obj 
	//that contains the info abou the status, setting the info of the track
	//Adding the track obj to the collection order tracks
	//And set status of the order in db and save it 
	public void updateStatus(Integer orderId, String status) {
		Order orderInDB = orderRepo.findById(orderId).get();
		OrderStatus statusToUpdate = OrderStatus.valueOf(status);
		
		if (!orderInDB.hasStatus(statusToUpdate)) {
			List<OrderTrack> orderTracks = orderInDB.getOrderTracks();
		
		OrderTrack track = new OrderTrack();
		track.setStatus(statusToUpdate);
		track.setUpdatedTime(new Date());
		track.setNotes(statusToUpdate.defaultDescription());
		
		orderTracks.add(track);
		orderInDB.setStatus(statusToUpdate);
		orderRepo.save(orderInDB);
		}
	}
}
