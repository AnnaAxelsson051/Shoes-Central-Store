package com.shopme.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.common.exception.CustomerNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ForgotPasswordController {
	
	@Autowired private CustomerService customerService;

	@GetMapping("/forgot_password")
	public String showRequestForm() {
		return "customer/forgot_password_form";
	}
	
	//When user chooses to reset password
	@PostMapping("/forgot_password")
	public String processRequestForm(HttpServletRequest request, Model model) {
		String email = request.getParameter("email");
		try {
			String token = customerService.updateResetPasswordToken(email);
		} catch (CustomerNotFoundException e) {
			model.addAttribute("error", e.getMessage());
		}
		
		return "customer/forgot_password_form";
	}
}
