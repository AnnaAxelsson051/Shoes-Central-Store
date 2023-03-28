package com.shopme.admin.user;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import com.shopme.common.entity.User;

@Controller
public class UserController {
	
	@Autowired    //inject an instance of user service at runtime 
	private UserService service;
	
	//link to user in navbar
	@GetMapping("/users")
	public String listAll(Model model) {
		List<User> listUsers = service.listAll();
		model.addAttribute("listUsers", listUsers);
		return "users";
		
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
		return "user_form";
		
	}
	
	//Method for saving user
	//map values of form fields, pic, to user objects and displays 
	//success message
	@PostMapping("/users/save")
	public String saveUser(User user, 
			RedirectAttributes redirectAttributes, 
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		System.out.println(user);  //to string in User class
		System.out.println(multipartFile.getOriginalFilename());
		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		//String fileName = multipartFile.getOriginalFilename();
		
		String uploadDir = "user-photos";
		
		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		//service.save(user);
		//redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
		return "redirect:/users";
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
		
		return "user_form";
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
}














