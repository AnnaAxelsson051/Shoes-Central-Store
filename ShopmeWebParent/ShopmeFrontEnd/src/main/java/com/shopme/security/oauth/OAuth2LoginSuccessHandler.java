package com.shopme.security.oauth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.shopme.customer.CustomerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired private CustomerService customerService;
	
	//Will be envoked by sprin security upon successful authentication
	//getting principal object from authentication object casting it to a 
	//customeroauth2user, get customer by email and if no customer creating
	//a new in db, otherwise update customer auth type and full name
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		CustomerOAuth2User oauth2User = (CustomerOAuth2User) authentication.getPrincipal();
		
		String name = oauth2User.getName();
		String email = oauth2User.getEmail();
		String countryCode = request.getLocale().getCountry();
		String clientName = oauth2User.getClientName();
		
		AuthenticationType authenticationType = getAuthenticationType(clientName);
		
		Customer customer = customerService.getCustomerByEmail(email);
		if(customer == null) {
			customerService.addNewCustomerUponOAuthLogin(name,email, countryCode, authenticationType);
		} else {
			oauth2User.setFullName(customer.getFullName());
			customerService.updateAuthenticationType(customer, authenticationType);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	private AuthenticationType getAuthenticationType(String clientName) {
		if (clientName.equals("Google")) {
			return AuthenticationType.GOOGLE;
		}else if (clientName.equals("Facebook")) {
			return AuthenticationType.FACEBOOK;
	}else {
		return AuthenticationType.DATABASE;
	}
	}

}
