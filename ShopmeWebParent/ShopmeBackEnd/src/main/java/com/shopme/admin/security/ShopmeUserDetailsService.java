package com.shopme.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.User;

//Class to perform authentication when logging in
//called by spring security

public class ShopmeUserDetailsService implements UserDetailsService {

	
	@Autowired
	private UserRepository userRepo;
	
	//returns new shopmedetails service wrapping user object
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	User user = userRepo.getUserByEmail(email);
	if(user!= null) {
		return new ShopmeUserDetails(user);
	}
		throw new UsernameNotFoundException("Could not find user with email" + email);
	}

}
