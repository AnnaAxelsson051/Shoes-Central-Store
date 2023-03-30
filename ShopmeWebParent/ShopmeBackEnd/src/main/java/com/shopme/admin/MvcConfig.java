package com.shopme.admin;


import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig {

	
	//Exposes absolute path to user photos so they are visible in browser
	
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dirName = "user-photos";
		Path userPhotosDir = Paths.get(dirName);
		
		String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/" + dirName + "/**")
		.addResourceLocations("file:/" + userPhotosPath + "/");
	}
	
	//DEPRECATED

//@Configuration
//public class MvcConfig extends WebMvcConfigurer {
	
	//Exposes absolute path to user photos so they are visible in browser
	//@Override
	//public void addResourceHandlers(ResourceHandlerRegistry registry) {
	//	String dirName = "user-photos";
	//	Path userPhotosDir = Paths.get(dirName);
		
	//	String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
		
	//	registry.addResourceHandler("/" + dirName + "/**")
	//	.addResourceLocations("file:/" + userPhotosPath + "/");
//	}
	
	
	

}
