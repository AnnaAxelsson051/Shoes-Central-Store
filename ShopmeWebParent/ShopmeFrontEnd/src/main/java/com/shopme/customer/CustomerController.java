package com.shopme.customer;

import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.Utility;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;

//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
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
}
