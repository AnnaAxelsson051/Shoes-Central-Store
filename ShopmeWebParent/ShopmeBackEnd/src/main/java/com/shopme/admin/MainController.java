package com.shopme.admin;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

@Controller
public class MainController {
	
	//handle request to homepage
	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	
	//If user is not logged in return login site otherwose 
	//redirect to homepage
	@GetMapping("/login")
	public String viewLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";	
		}
		return "redirect:/";
	}

}
