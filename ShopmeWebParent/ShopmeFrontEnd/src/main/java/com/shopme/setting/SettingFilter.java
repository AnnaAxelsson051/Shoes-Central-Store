package com.shopme.setting;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shopme.common.entity.setting.Setting;

import java.util.List;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

//Filter for settingvalues
@Component
public class SettingFilter implements Filter {
	
	@Autowired
	private SettingService service;
	
	@Override
	public void doFilter(ServletRequest request, 
			ServletResponse response, FilterChain chain) 
					throws IOException, ServletException{
		
		HttpServletRequest servletRequest = (HttpServletRequest) request;
	String url = servletRequest.getRequestURL().toString();
	
	
	if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png")
			|| url.endsWith(".jpg")) {
		chain.doFilter(request,  response);
		return;
	}
	List <Setting> generalSettings = service.getGeneralSettings();
	generalSettings.forEach(setting -> {
		request.setAttribute(setting.getKey(), setting.getValue());
	});
	chain.doFilter(request,  response);
	}

}
