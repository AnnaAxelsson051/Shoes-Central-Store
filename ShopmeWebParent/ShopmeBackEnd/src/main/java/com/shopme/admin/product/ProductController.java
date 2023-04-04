package com.shopme.admin.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import com.shopme.admin.brand.BrandService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;

@Controller
public class ProductController {
	@Autowired private ProductService productService;
	@Autowired private BrandService brandService;
	
	@GetMapping("/products")
	public String listAll(Model model) {
		List<Product> listProducts = productService.listAll();
		
		return "products/products";
	}

	//creating products
	//displaying a dropdown list of all brands
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Brand> listBrands = brandService.listAll();
		
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		
		model.addAttribute("product",product);
		model.addAttribute("listBrands", product);
		model.addAttribute("pageTitle", "Create New Product");
		
		return "products/product_form";
	}
	
	//Handeler method for submission of product form
	@PostMapping("/products/save)")
	public String saveProduct(Product product) {
		System.out.println("Product Name: " + product.getName());
		System.out.println("Brand ID: " + product.getBrand().getId());
		System.out.println("Category ID: " + product.getCategory().getId());
	
	return "redirect:/products";
	}
}
