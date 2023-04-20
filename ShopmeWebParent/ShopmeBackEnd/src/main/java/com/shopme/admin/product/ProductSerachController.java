package com.shopme.admin.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;

@Controller
public class ProductSerachController {
	
	@Autowired private ProductService service;
	
	@GetMapping("/orders/search_product")
	public String showSearchProductPage() {
		return "orders/search_product";	
		}
	
	//Handeling submission of search form
	@PostMapping("/orders/search_product")
	public String searchProducts(String keyword) {
		return "redirect:/orders/search_product/page/1?sortField=name&sortDir=asc&keyword=" + keyword;
	}
	
	
//Displaying search result per page
	@GetMapping("/orders/search_product/page/{pageNum}")
	public String searchProductsByPage(@PagingAndSortingParam(listName = "listProducts", 
	moduleURL = "/orders/search_product") PagingAndSortingHelper helper,
	@PathVariable(name="pageNum") int pageNum) {
		service.searchProducts(pageNum, helper);
		return "orders/search_product";
		
}

}