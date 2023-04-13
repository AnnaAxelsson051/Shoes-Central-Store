package com.shopme.customer;

import java.util.List;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.CountryRepository;

import jakarta.annotation.Resource.AuthenticationType;

//import net.bytebuddy.utility.RandomString;

@Service
public class CustomerService {

	@Autowired 
	private CountryRepository countryRepo;
	
	@Autowired 
	private CustomerRepository customerRepo;
	
	@Autowired 
	PasswordEncoder passwordEncoder;
	
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
	}
	
	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		
		String randomCode = RandomString.make(64);
		customer.setVerificationCode(randomCode);
		
		customerRepo.save(customer);
	}
	
	
	//Setting encoded pw to user obj
	private void encodePassword(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}
	
	//verifying cusomeraccount
	//getting customer by verificationcode and updates the enabled
	//status on that customer
	public boolean verify(String verificationCode) {
		Customer customer = customerRepo.findByVerificationCode(verificationCode);
	
		if(customer == null || customer.isEnabled()) {
			return false;	
	} else {
		customerRepo.enable(customer.getId());
		return true;
	}
		
	}
	//Checking if the auth type here is different than tha type in customer object then calling
	//customer repo to update the auth type
	public void updateAuthenticationType(Customer customer, com.shopme.common.entity.AuthenticationType type) {
		if (!customer.getAuthenticationType().equals(type)) {
			customerRepo.updateAuthenticationType(customer.getId(), type);
		}
	}
}
