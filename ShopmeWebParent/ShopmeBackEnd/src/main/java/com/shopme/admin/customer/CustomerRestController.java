package com.shopme.admin.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;

public class CustomerRestController {

	@Autowired
	private CustomerService service;
	
	//Returning ok if emial is unique
	@PostMapping("/customers/check_email")
	public String checkDuplicateEmail(Integer id, String email
			) {
		if(service.isEmailUnique(id,  email)) {
			return "OK";
		} else {
			return "Duplicated";
		}
	}
}
