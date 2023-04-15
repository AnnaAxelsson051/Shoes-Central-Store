package com.shopme.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {
	
	@Autowired private CustomerService service;
	
	//Checking if the email user filled out in 
	//form is unique or already in the system
	@PostMapping("/customer/check_unique_email")
public String checkDuplicateEmail(String email) {
	return service.isEmailUnique(email) ? "OK" : "Duplicated";
}
}
