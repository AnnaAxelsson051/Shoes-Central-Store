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

	
	//Exposes absolute path to photos so they are avaliable
	//for the webclient browser to access
	
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		//user photos
		String dirName = "user-photos";
		Path userPhotosDir = Paths.get(dirName);
		
		String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/" + dirName + "/**")
		.addResourceLocations("file:/" + userPhotosPath + "/");
	
	
	//Item images
	String categoryImagesDirName = "../category-images";
	Path categoryImagesDir = Paths.get(categoryImagesDirName);
	
	String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();
	
	registry.addResourceHandler("/category-images/**")
	.addResourceLocations("file:/" + categoryImagesPath + "/");
	
	
	//Brand logos
	String brandLogosDirName = "../brand/logos";
	Path brandLogosDir = Paths.get(brandLogosDirName);
	
	String brandLogosPath = brandLogosDir.toFile().getAbsolutePath();
	
	registry.addResourceHandler("/brand-logos/**")
	.addResourceLocations("file:/" + brandLogosPath + "/");
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
