package com.shopme.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.CountryRepository;

@Service
public class CustomerService {

	@Autowired 
	private CountryRepository countryRepo;
	
	@Autowired 
	private CustomerRepository customerRepo;
	
	//Listing all countries in form for customer to 
	//select from when creating account
	public List <Country> listAllCountries(){
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	//Checking if email user filled in to form is unique by 
	// or already in system
	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.findByEmail(email);
		return customer == null;
;	}
}
