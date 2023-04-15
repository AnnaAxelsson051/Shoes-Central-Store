package com.shopme.admin.category;

import java.io.IOException;
import com.shopme.common.exception.CategoryNotFoundException;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.Category;



@Controller
public class CategoryController {
	
	@Autowired
	private CategoryService service;
	
	@GetMapping("/categories")
	public String listFirstPage( String sortDir, Model model) {
		return listByPage(1, sortDir, null, model);
	}
	
	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(@PathVariable(name="pageNum")
	int pageNum, 
	String sortDir, 
	String keyword,
	Model model) {
		if(sortDir == null || sortDir.isEmpty()) {
			sortDir= "asc";
		}
		
		CategoryPageInfo pageInfo = new CategoryPageInfo();
		List<Category> listCategories = service.listByPage(pageInfo, pageNum, sortDir, keyword);
		
		long startCount = (pageNum -1) * CategoryService.ROOT_CATEGORIES_PER_PAGE +1;
		long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
		if (endCount > pageInfo.getTotalElements()) {
			endCount = pageInfo.getTotalElements();
		}
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("totalPages", pageInfo.getTotalPages());
		model.addAttribute("totalItems", pageInfo.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", "name");
		model.addAttribute("sortField", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		
		return "categories/categories";
	}

	//Lists categories adds to model
@GetMapping("/categories/new")
public String newCategory(Model model) {
	List<Category> listCategories = service.listCategoriesUsedInForm();
	
	model.addAttribute("category", new Category());
	model.addAttribute("listCategories", listCategories);
	model.addAttribute("pageTitle", "Create New Category");
	
	return "categories/category_form";
}

//Saves category with save file, creates upload directory if not exixts
//on same level as shopme backend n shopme frontend
//if no image just saves category
@PostMapping("/categories/save")
public String saveCategory(Category category,
		@RequestParam("fileImage") MultipartFile multipartFile, 
		RedirectAttributes ra) throws IOException {
	if (!multipartFile.isEmpty()) {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		category.setImage(fileName);
		
		Category savedCategory = service.save(category);
		String uploadDir = "../category-images/" + savedCategory.getId();
		FileUploadUtil.saveFile(uploadDir, fileName,  multipartFile);
	}else {
		service.save(category);
	}
	ra.addFlashAttribute("message", "The category has been saved successfully");
			return "redirect:/categories";
}

//Shows category form, creates cat obj from db and a list of 
//categories to be used in the form 
//put cat obj and list onto model and updates page title
//if no category found redirects to listing page
@GetMapping("/categories/edit/{id}")
public String editCategory(@PathVariable(name = "id") 
Integer id, Model model, RedirectAttributes ra) {
	try {
		Category category = service.get(id);
		List<Category> listCategories = service.listCategoriesUsedInForm();
		
		model.addAttribute("category", category);
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Edit Category (ID: " + id +")");
		
		return "categories/category_form";
	}catch (CategoryNotFoundException ex) {
		ra.addFlashAttribute("message", ex.getMessage());
		return "redirect:/categories";
	}
}


@GetMapping("/categories/{id}/enabled/{status}")
public String updateCetegoryEnabledStatus(@PathVariable("id") Integer id,
		@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
	service.updateCategoryEnabledStatus(id, enabled);
	String status = enabled ? "enabled" : "disabled";
	String message = "The category ID " + id + " has been " + status;
	redirectAttributes.addFlashAttribute("message", message);
	
	return "redirect:/categories";
	}

//Deletes cat, removes image dir
@GetMapping("/categories/delete/{id}")
public String deleteCategory(@PathVariable(name = "id") Integer id, 
		Model model, RedirectAttributes redirectAttributes) {
	try {
		service.delete(id);
		String categoryDir = "../category-images/" + id;
		FileUploadUtil.removeDir(categoryDir);
		
		redirectAttributes.addFlashAttribute("message", 
				"The category Id " + id + " has been deleted successfully");
	}catch (CategoryNotFoundException ex) {
		redirectAttributes.addFlashAttribute("message", ex.getMessage());
	}
	return "redirect:/categories";
}

@GetMapping("/categories/export/csv")
public void exportToCSV(HttpServletResponse response) throws IOException {
	List<Category> listCategories = service.listCategoriesUsedInForm();
	CategoryCsvExporter exporter = new CategoryCsvExporter();
	exporter.export(listCategories, response);
}

}

