package com.shopme.shipping;

import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;

public interface ShippingRateRepository extends CrudRepository <ShippingRate, Integer>{

	
	//No need for custom query will be understood by spring jpa
	public ShippingRate findByCountryAndState(Country country, String state);
}
