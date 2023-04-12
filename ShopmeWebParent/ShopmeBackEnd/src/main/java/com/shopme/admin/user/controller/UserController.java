package com.shopme.admin.user.controller;

import java.io.IOException;
import com.shopme.common.entity.Role;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.admin.user.UserService;
import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.admin.user.export.UserExcelExporter;
import com.shopme.admin.user.export.UserPdfExporter;
import com.shopme.common.entity.User;

import jakarta.persistence.Transient;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {
	
	@Autowired    //inject an instance of user service at runtime 
	private UserService service;
	
	//link to user in navbar
	@GetMapping("/users")
	public String listFirstPage() {
		return "redirect:/users/page/1?sortField=firstName&sortDir=asc";
		
	}
	
	//Listing users by page (pageNum)
	//Using Paging and sorting helper class
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam (listName = "listUsers", moduleURL = "/users") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum) {
		service.listByPage(pageNum, helper);
	
	return "users/users";
	
	}
	
	//New user method
	//maps to users.html
	//map value of formfield to model / form
	//put listroles onto model
	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles();
		
		User user = new User();
		user.setEnabled(true);   //default is checked box for enabled value
		model.addAttribute("user",user);
		model.addAttribute("listRoles",listRoles); //add list roles to model
		model.addAttribute("pageTitle","Create New User"); //set page title
		return "users/user_form";
		
	}
	
	//Method for saving user
	//map values of form fields, pic, to user objects and displays 
    //saves photo of user in directory with user id
	//Post user updated shows only the affected user
	@PostMapping("/users/save")
	public String saveUser(User user, 
			RedirectAttributes redirectAttributes, 
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		if(!multipartFile.isEmpty()) {
			//String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String fileName = multipartFile.getOriginalFilename();
			user.setPhotos(fileName);
			User savedUser = service.save(user);
			
			String uploadDir = "user-photos/" + savedUser.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			//service.save(user);
		}else {
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			service.save(user);
		}
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
		return getRedirectURLtoAffectedUser(user);
	}
	
	//filters out affected user by first part in email
	private String getRedirectURLtoAffectedUser(User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
	}
	
	
	
	
	
	//Edit user puts user object onto model
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") 
	Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
		User user = service.get(id);
		List<Role> listRoles = service.listRoles();
		model.addAttribute("user", user);
		model.addAttribute("pageTitle","Edit User (Id: " + id + ")"); 
		model.addAttribute("listRoles",listRoles); 
		
		return "users/user_form";
		}catch(UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";  
		}
	}
	
	//Method for deleting user
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") 
	Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id); 
			redirectAttributes.addFlashAttribute
			("message", "The user Id " + id + " has been successfully deleted");
			return "user_form";
			}catch(UserNotFoundException ex) {
				redirectAttributes.addFlashAttribute("message", ex.getMessage());
			}
		return "redirect:/users";  
		}
	
	//Method for enabeling/disabeling user
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") 
	Integer id, @PathVariable("status") boolean enabled, 
	RedirectAttributes redirectAttributes){
		service.updateUserEnabledStatus(id, enabled);
	String status = enabled ? "enabled" : "disabled";
	String message = "The user Id " + id + " has been " + status;
	redirectAttributes.addFlashAttribute("message", message);
	return "redirect:/users";
	}
	

	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException{
		List <User> listUsers = service.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
	exporter.export(listUsers,response);
		
	}
	
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException{
		List <User> listUsers = service.listAll();
		UserExcelExporter exporter = new UserExcelExporter();
	exporter.export(listUsers, response);
		
	}
	
	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException{
		List <User> listUsers = service.listAll();
		UserPdfExporter exporter = new UserPdfExporter();
	exporter.export(listUsers, response);
		
	}
	
	
}














