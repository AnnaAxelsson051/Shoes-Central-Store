package com.shopme.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	//maps to users.html
	//map value of formfield to model / form
	//put listroles onto model
	
	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles();
		
		User user = new User();
		user.setEnabled(true);   //default is checked box for enabled value
		model.addAttribute("user",user);
		model.addAttribute("listRoles",listRoles);
		model.addAttribute("pageTitle","Create New User"); //set page title
		return "user_form";
		
	}
	
	//when pushing save button, map values of form fields to user objects and displays 
	//success message
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes ) {
		System.out.println(user);  //to string in User class
		service.save(user);
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
		return "redirect:/users";
	}
	
	//edit user puts user object onto model
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") 
	Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
		User user = service.get(id);
		model.addAttribute("user", user);
		model.addAttribute("pageTitle","Edit User (Id: " + id + ")"); //set page title
		return "user_form";
		}catch(UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}

}













