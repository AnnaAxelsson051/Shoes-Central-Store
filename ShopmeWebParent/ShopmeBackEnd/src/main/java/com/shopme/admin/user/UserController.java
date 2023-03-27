package com.shopme.admin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
	
	@Autowired    //inject an instance of user service at runtime 
	private UserService service;
	
	//link to user in navbar
	@GetMapping("/users")
	public String listAll() {
		return "users";
		
	}
	

}
