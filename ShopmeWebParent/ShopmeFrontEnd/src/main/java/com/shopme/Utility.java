package com.shopme;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.shopme.setting.EmailSettingBag;

import jakarta.servlet.http.HttpServletRequest;

public class Utility {

	
	//Returnning url that will be used in the verify url
	public static String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		
		return siteURL.replace(request.getServletPath(),"");
	}
	//Configuring an instance of javamailsender class with email settings
public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
	JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

	mailSender.setHost(settings.getHost());
	mailSender.setPort(settings.getPort());
	mailSender.setUsername(settings.getUsername());
	mailSender.setPassword(settings.getPassword());
	
	Properties mailProperties = new Properties();
	mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
	mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());
	 
	mailSender.setJavaMailProperties(mailProperties);
	
	return mailSender;
}
}
