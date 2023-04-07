package com.shopme;

import java.util.List;
import com.shopme.common.entity.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.category.CategoryService;

import org.springframework.ui.Model;

@Controller
public class MainController {
	
	@Autowired private CategoryService categoryService;
	
	//Adding list of enabled categories sorted in ASC onto model
	@GetMapping("")
	public String viewHomePage(Model model) {
		List<Category> listCategories = categoryService.listNoChildrenCategories();
		model.addAttribute("listCategories", listCategories);
		
		return "index";
	}

}
