package com.shopme.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {
	@Autowired
	private CustomerService customerService;

	@Autowired
	private ShoppingCartService cartService;
	
	//Adds products to cart if customer is logged in
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(
			@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
			Integer updatedQuantity = cartService.addProduct(productId, quantity, customer);
			return updatedQuantity + " item(s) were added to your shopping cart.";
		} catch (CustomerNotFoundException ex) {
			return "You must log in to add this product to cart.";		
		} catch (ShoppingCartException ex) {
			return ex.getMessage();
		}

	}
	
	//gets email of logged in user
	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		if (email == null) {
			throw new CustomerNotFoundException("No authenticated customer");
		}
		return customerService.getCustomerByEmail(email);
	}
	
	//Adds products to cart if customer is logged in
		@PostMapping("/cart/update/{productId}/{quantity}")
		public String updateQuantity(
				@PathVariable("productId") Integer productId,
				@PathVariable("quantity") Integer quantity, HttpServletRequest request) {
			try {
				Customer customer = getAuthenticatedCustomer(request);
				float subtotal = cartService.updateQuantity(productId,  quantity, customer);
				
				return String.valueOf(subtotal);
			} catch (CustomerNotFoundException ex) {
				return "You must log in to change the quantity of product"
						+ ".";		
			} 
		
		}
}
