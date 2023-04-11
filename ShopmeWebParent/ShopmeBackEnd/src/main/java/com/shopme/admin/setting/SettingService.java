package com.shopme.admin.setting;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.GeneralSettingBag;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

@Service
public class SettingService {
	@Autowired private SettingRepository repo;
	
	//For request to view settings
	public List<Setting> listAllSettings() {
		return (List<Setting>) repo.findAll();
	}
	
	
	//Returnmethod for the General settin object 
	//Adding settings and curency settings to model
	public GeneralSettingBag getGeneralSettings() {
		List<Setting> settings = new ArrayList<>();
		
		List<Setting> generalSettings = repo.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySettings = repo.findByCategory(SettingCategory.CURRENCY);
		
		settings.addAll(generalSettings);
		settings.addAll(currencySettings);
		
		return new GeneralSettingBag(settings);
	}
	
	//Saving collection of setting objects
	public void saveAll(Iterable<Setting> settings ) {
		repo.saveAll(settings);
	}
	
	//Returning a list of mail server setting
	//For mail server form
	public List <Setting> getMailTemplateSettings(){
		return repo.findByCategory(SettingCategory.MAIL_SERVER);
	}
	
	//Returning a list of mail template settngs
	public List <Setting> getMailServerSettings(){
		return repo.findByCategory(SettingCategory.MAIL_TEMPLATES);
	}
}