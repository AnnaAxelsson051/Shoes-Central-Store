package com.shopme.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.common.entity.Category;

@Controller
public class ProductController {
	
	@Autowired private ProductService productService;
	@Autowired private BrandService brandService;
	@Autowired private CategoryService categoryService;
	
	@GetMapping("/products")
	public String listFirstPage(Model model) {
		return "redirect:/products/page/1?sortField=name&sortDir=asc";
	}
	
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(		
			@PagingAndSortingParam(listName = "listProducts", 
			moduleURL = "/products") PagingAndSortingHelper helper,
			@PathVariable(name="pageNum") int pageNum, Model model, 
			Integer categoryId
			) {
		productService.listByPage(pageNum, helper, categoryId);
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		
	if(categoryId != null) model.addAttribute("categoryId, categoryId");
	model.addAttribute("listCategories", listCategories);
	
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
		model.addAttribute("numberOfExistingExtraImages", 0);
		
		return "products/product_form";
	}
	
	//Saving a product with form
	//for submission of product form
	//Saving image names - and images
	@PostMapping("/products/save)")
	public String saveProduct(Product product, RedirectAttributes ra,
			@RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
			@RequestParam(value = "extraImage", required = false) MultipartFile [] extraImageMultiparts,
			@RequestParam(name = "detailIDs", required = false) String [] detailIDs,
			@RequestParam(name = "detailNames", required = false) String [] detailNames,
			@RequestParam(name = "detailValues", required = false) String [] detailValues,
		    @RequestParam(name = "imageIDs", required = false) String [] imageIDs,
		    @RequestParam(name = "imageNames", required = false) String [] imageNames,
		    @AuthenticationPrincipal ShopmeUserDetails loggedUser)throws IOException {
		   
		//Saving price if salesperson
		if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
			if (loggedUser.hasRole("Salesperson")) {
			productService.saveProductPrice(product);
			ra.addFlashAttribute("message", "The product has been saved successfully");
		    return "redirect:/products";
		}
		}
		
		ProductSaveHelper.setMainImageName(mainImageMultipart, product);
		ProductSaveHelper.setExistingExtraImageNames(imageIDs, imageNames, product);
		ProductSaveHelper.setNewExtraImageNames(extraImageMultiparts, product);
		ProductSaveHelper.setProductDetails(detailIDs, detailNames, detailValues, product);
			
			Product savedProduct = productService.save(product);
			
			ProductSaveHelper.saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);
		
			ProductSaveHelper.deleteExtraImageWhenRemovedFromForm(product);
			
			ra.addFlashAttribute("message", "The product has been saved successfully");
		    return "redirect:/products";
	} 
	
	
	
	//Updates enabled/disabled status for product 
	@GetMapping("/products/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(
			@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, 
			RedirectAttributes redirectAttributes) {
		productService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The product ID " + id + " has been "	
		+ status;
		redirectAttributes.addFlashAttribute("message", message);
		
		return "redirect:/products";
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(
			@PathVariable(name = "id") Integer id, 
			Model model, RedirectAttributes redirectAttributes) {
		try {
			productService.delete(id);
			
			redirectAttributes.addFlashAttribute("message", "The product ID " + id 
					+ " has been deleted successfully");
		} catch (ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/products";
	}
	
	//Handles request for edit a product
	@GetMapping("/products/edit/{id}")
	public String editProduct(
			@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra, @AuthenticationPrincipal ShopmeUserDetails loggedUser) {
		
		try {
			Product product = productService.get(id);
			List<Brand> listBrands = brandService.listAll();
			Integer numberOfExistingExtraImages = product.getImages().size();
			
			boolean isReadOnlyForSalesPerson = false;
			//Saving price if salesperson
			if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
				if (loggedUser.hasRole("Salesperson")) {
					isReadOnlyForSalesPerson = true;
			}
			}
			model.addAttribute("isReadOnlyForSalesPerson", isReadOnlyForSalesPerson);
			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

			return "products/product_form";
			
			} catch  (ProductNotFoundException e) {
				ra.addFlashAttribute("message", e.getMessage());
				
				return "redirect:/products";
			}
	}
	
	@GetMapping("/products/detail/{id}")
	public String viewProductDetails(
			@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Product product = productService.get(id);
			
			model.addAttribute("product", product);

			return "products/product_detail_modal";
			
			} catch  (ProductNotFoundException e) {
				ra.addFlashAttribute("message", e.getMessage());
				
				return "redirect:/products";
			}
}
}
