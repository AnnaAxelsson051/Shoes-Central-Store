package com.shopme.shipping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;

@Service
public class ShippingRateService {

	@Autowired
	private ShippingRateRepository repo;
	
	//Getting shipping rate for customer by
	//getting state or city calling findByCountry nd state 
	public ShippingRate getShippingRateForCustomer(Customer customer) {
		String state = customer.getState();
		if (state == null || state.isEmpty()) {
		state = customer.getCity();
	}
		return repo.findByCountryAndState(customer.getCountry(), state);
}
	
	//Getting shipping rate for customer by
	//getting state or city calling findByCountry nd state 
	public ShippingRate getShippingRateForAddress(Address address) {
		String state = address.getState();
		if (state == null || state.isEmpty()) {
		state = address.getCity();
	}
		return repo.findByCountryAndState(address.getCountry(), state);
}
}
