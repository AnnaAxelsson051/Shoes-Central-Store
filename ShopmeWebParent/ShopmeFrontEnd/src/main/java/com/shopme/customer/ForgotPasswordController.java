package com.shopme.customer;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.Utility;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import com.shopme.common.entity.Customer;

@Controller
public class ForgotPasswordController {
	
	@Autowired private CustomerService customerService;
	@Autowired private SettingService settingService;

	@GetMapping("/forgot_password")
	public String showRequestForm() {
		return "customer/forgot_password_form";
	}
	
	//When user chooses to reset password, creating a reset token and link for
	//user to click to reset pw, showing a confirmation message 
	@PostMapping("/forgot_password")
	public String processRequestForm(HttpServletRequest request, 
			Model model) {
		String email = request.getParameter("email");
		try {
			String token = customerService.updateResetPasswordToken(email);
		    String link = Utility.getSiteURL(request) + "/reset_password?token=" + token;
		    sendEmail(link, email);
		    
		    model.addAttribute("message", "We have sent a reset password link to your email."
		    		+ " Please check.");
		} catch (CustomerNotFoundException e) {
			model.addAttribute("error", e.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error", "Could not send email");
		}
		
		return "customer/forgot_password_form";
	}
	
	//Sending an email to user to reset password
	private void sendEmail(String link, String email) throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		
		String toAddress = email;
		String subject = "Here's the link to reset your password";
		
		String content = "<p>Hello,</p>"
				+ "<p>You have requested to reset your password.</p>"
				+ "Click the link below to change your password:</p>"
				+ "<p><a href=\"" + link + "\">Change my password</a></p>"
				+ "<br>"
				+ "<p> Ignore thir email if you do remember your password,"
				+ "or you have not made the request.</p>";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
	
	helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
	helper.setTo(toAddress);
	helper.setSubject(subject);
	
	helper.setText(content, true);
	mailSender.send(message);
		
	}
	
	//Showing the reset password form when customer clicks change password
	//link in the email, accessing the token in the url
	@GetMapping("/reset_password")
	public String showResetForm(String token, Model model) {
		Customer customer = customerService.getByResetPasswordToken(token);
		if (customer != null) {
			model.addAttribute("token", token);
		}else {
			model.addAttribute("pageTitle", "Invalid token");
			model.addAttribute("message", "Invalid token");
			return "message";
		}
		
		return "customer/reset_password_form";
		
	}
	
	//Accesses the http servlet request object reads the token and pw from html reset_pw
	//And updates pw by calling service if there is such a token
	//returning message html
	@PostMapping("/reset_password")
	public String processResetForm(HttpServletRequest request, Model model) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		
		try {
			customerService.updatePassword(token, password);
			model.addAttribute("pageTitle", "Reset your password");
			model.addAttribute("message", "Reset your password");
			model.addAttribute("title", "You have successfully changed your password.");
			
			return "message";
		} catch (CustomerNotFoundException e) {
			model.addAttribute("pageTitle", "invalidToken");
			model.addAttribute("message", e.getMessage());
			return "message";
		}
	}
}
