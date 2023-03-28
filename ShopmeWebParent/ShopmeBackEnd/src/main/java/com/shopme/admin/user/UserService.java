package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List <User> listAll(){
		return (List <User>) userRepo.findAll();
	}
	
	public List <Role> listRoles(){
		return (List<Role>) roleRepo.findAll();
	}

	
	
	//encodes pw and saves user to db
	public void save(User user) {
		encodePassword(user);
		userRepo.save(user);
	}
	
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	
	//if returns null user email is unique
	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepo.getUserByEmail(email);
		
		if(userByEmail == null)return true;
		
		boolean isCreatingNew = (id == null); //if user being edited
		if(isCreatingNew) {
			if(userByEmail != null) return false;
		}else {
			if (userByEmail.getId() != id) {
				return false; 
			}
		}
		return true;
	}

	public User get(Integer id) throws UserNotFoundException {
		try {
		return userRepo.findById(id).get();
		}catch(NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user with id " + id);
		}
	}
	
	/*
	public void delete(Integer id) {
		Long countById = userRepo.countById(id);
		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID" + id);
		}
		userRepo.deleteById(id);
	}*/
}
