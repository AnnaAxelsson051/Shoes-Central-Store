package com.shopme.setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;


@Service
public class SettingService {
	@Autowired private SettingRepository repo;
	
	
	//Returnmethod for the General settin object 
	//Adding settings and curency settings to model
	public List<Setting> getGeneralSettings() {
		
		
		return repo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}
	
	//Returning a new settingbag obj that takes the settings list collection
	public EmailSettingBag getEmailSettings() {
		List<Setting> settings = repo.findByCategory(SettingCategory.MAIL_SERVER);
		settings.addAll(repo.findByCategory(SettingCategory.MAIL_TEMPLATES));
	
	return new EmailSettingBag(settings);
	}
	
	public CurrencySettingBag getCurrencySettings() {
		List<Setting> settings = repo.findByCategory(SettingCategory.CURRENCY);
	return new CurrencySettingBag(settings);
	}
	
	public PaymentSettingBag getPaymentSettings() {
		List<Setting> settings = repo.findByCategory(SettingCategory.PAYMENT);
		return new PaymentSettingBag(settings);
	}
}