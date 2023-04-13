package com.shopme.admin.customer;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;

//import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class CustomerService {
	public static final int CUSTOMERS_PER_PAGE = 10;

	@Autowired private com.shopme.admin.setting.country.CountryRepository countryRepo;
	@Autowired private CustomerRepository customerRepo;
	@Autowired private PasswordEncoder passwordEncoder;
	
	
	public Page <Customer> listByPage(int pageNum, String sortField, 
			String sortDir, String keyword){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum -1,  CUSTOMERS_PER_PAGE, sort);
	
	if (keyword != null) {
		return customerRepo.findAll(keyword, pageable);
	}
	return customerRepo.findAll(pageable);
	}
	
	
	
	public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
		customerRepo.updateEnabledStatus(id, enabled);
	}
	
	public Customer get(Integer id) throws CustomerNotFoundException{
		try {
			return customerRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CustomerNotFoundException("Could not find any customers with ID " + id);
		}
	}
	
	//Returning list of countries to be used in the customer edit form 
	public List<Country> listAllCountries(){
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	//Used by the restful websercice controller for checking
	//the uniqueness of the customer email
	public boolean isEmailUnique(Integer id, String email) {
		Customer existCustomer = customerRepo.findByEmail(email);
		
		if(existCustomer != null && existCustomer.getId() != id) {
			return false; //found another cust with same email
		}
		return true;
	}
	
	//Updating the changes of details of customer in form
	//Before saving customer in form object copies created time, enabled status,
	//verification code and authenticationtype
	//from the customer in db object (dvs values that are not in the form)
	//If password in form is not empty encoding the password and updates it  
	public void save (Customer customerInForm) {
		Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();
		if(!customerInForm.getPassword().isEmpty()) {
		String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
		customerInForm.setPassword(encodedPassword);
	}else {
		customerInForm.setPassword(customerInDB.getPassword());
	}
		customerInForm.setEnabled(customerInDB.isEnabled());
		customerInForm.setCreatedTime(customerInDB.getCreatedTime());
		customerInForm.setVerificationCode(customerInDB.getVerificationCode());
	    customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
	    
	customerRepo.save(customerInForm);
}
	public void delete (Integer id) throws CustomerNotFoundException{
		Long count = customerRepo.countById(id);
		if(count == null || count == 0) {
			throw new CustomerNotFoundException("Could not find any customers with id " + id);
		}
		customerRepo.deleteById(id);
	}
}