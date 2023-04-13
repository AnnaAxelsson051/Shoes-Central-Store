package com.shopme.customer;

import java.io.UnsupportedEncodingException;
import java.util.*;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.Utility;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.shopme.security.CustomerUserDetails;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
import com.shopme.security.oauth.CustomerOAuth2User;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;

//import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private SettingService settingService;
	
	
	//List of countries displayed in the registration form
	//åutting list, pagetitle and new customer obj onto the form
	@GetMapping("/register")
    public String showRegisterForm(Model model) {
	List<Country> listCountries = customerService.listAllCountries();
	
	model.addAttribute("listCountries",listCountries);
	model.addAttribute("pageTitle", "Customer Registration");
	model.addAttribute("customer", new Customer());
	
	return "register/register_form";
}
	
	//For sumbission of create customer form 
	@PostMapping("/create_customer")
	public String createCustomer(Customer customer, 
			Model model, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
	customerService.registerCustomer(customer);
	senderVerificationEmail(request, customer);
	
	model.addAttribute("pageTitle", "Registration Succeeded!");
	
	return "/register/register_success";
	}
	
	//Sending a verification email when new customer has registered
	//Getting email settings, creating a mimemessage object for sending html message 
	//including a verify url link for customer to click
	private void senderVerificationEmail(HttpServletRequest request,
			Customer customer) throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
	
		String toAddress = customer.getEmail();
		String subject = emailSettings.getCustomerVerifySubject();
		String content = emailSettings.getCustomerVerifyContent();
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
	
	helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
	helper.setTo(toAddress);
	helper.setSubject(subject);
	
	content = content.replace("[[name]]", customer.getFullName());
	
	String verifyURL = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
	
	content = content.replace("[[URL]]", verifyURL);
	
	helper.setText(content, true);
	mailSender.send(message);
	}
	
	//verifying customer account
	@GetMapping("/verify")
	public String verifyAccount(@Param("code") 
	String code, Model model) {
		boolean verified = customerService.verify(code);
		return "register/" + (verified ? "verify_success" : "verify_fail");
	}
	
	//For customer to view their account details
	//getting customer email via the authenticated user object
	//in different ways depending on how user is logged in
	@GetMapping("/account_details")
	public String viewAccountDetails(Model model, HttpServletRequest request) {
		String email = getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.getCustomerByEmail(email);
	List<Country> listCountries = customerService.listAllCountries();
		
		model.addAttribute("customer", customer);
		model.addAttribute("listCountries", listCountries);
		return "customer/account_form";
	}
	
	private String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
		Object principal = request.getUserPrincipal();
	String customerEmail = null;
	
	if (principal instanceof UsernamePasswordAuthenticationToken
	|| principal instanceof RememberMeAuthenticationToken) {
	customerEmail = request.getUserPrincipal().getName();
	} else if (principal instanceof OAuth2AuthenticationToken) {
		OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
	CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
	customerEmail = oauth2User.getEmail();
	}
	return customerEmail;
}
	
	//When customer is updating their account details
	@PostMapping("/update_account_details")
	public String updateAccountDetails(Model model, Customer customer, RedirectAttributes ra,
			HttpServletRequest request) {
		customerService.update(customer);
		ra.addFlashAttribute("message", "Your account details have been updated");
		updateNameForAuthenticatedCustomer(customer, request);
		return "redirect:/account_details";
	}

	//When user chooses to alter their name
	private void updateNameForAuthenticatedCustomer(Customer customer, HttpServletRequest request) {
		Object principal = request.getUserPrincipal();
	
	if (principal instanceof UsernamePasswordAuthenticationToken
	|| principal instanceof RememberMeAuthenticationToken) {
    CustomerUserDetails userDetails = getCustomerUserDetailsObject (principal);
    Customer authenticatedCustomer = userDetails.getCustomer();
	authenticatedCustomer.setFirstName(customer.getFirstName());
	authenticatedCustomer.setLastName(customer.getLastName());
	} else if (principal instanceof OAuth2AuthenticationToken) {
		OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
	CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
	String fullName = customer.getFirstName() + " " + customer.getLastName();
	oauth2User.setFullName(fullName);
	}
		
	}
	
	private CustomerUserDetails getCustomerUserDetailsObject(Object principal) {
		CustomerUserDetails userDetails = null;
		if(principal instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		} else if (principal instanceof RememberMeAuthenticationToken) {
			RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
			userDetails = (CustomerUserDetails) token.getPrincipal();
		}
		
		return userDetails;
	}
}
