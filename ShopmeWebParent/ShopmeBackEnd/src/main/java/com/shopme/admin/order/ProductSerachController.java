package com.shopme.admin.order;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductSerachController {
	
	@GetMapping("/orders/search_product")
	public String showSearchProductPage() {
		return "orders/search_product";	
		}

}
