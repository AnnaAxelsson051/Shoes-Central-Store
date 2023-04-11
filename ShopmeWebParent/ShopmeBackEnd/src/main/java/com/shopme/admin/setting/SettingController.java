package com.shopme.admin.setting;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.GeneralSettingBag;
import com.shopme.common.entity.Setting;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;


@Controller
public class SettingController {

	@Autowired private SettingService service;
	
	@Autowired private CurrencyRepository currencyRepo;
	
	//Handles request to view settings page, displays list of currencies
	@GetMapping("/settings")
	public String listAll(Model model) {
		List<Setting> listSettings = service.listAllSettings();
		List<Currency> listCurrencies = currencyRepo.findAllByOrderByNameAsc();
		
		model.addAttribute("listCurrencies", listCurrencies);
		
		for (Setting setting : listSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}
		
		return "settings/settings";
	}
	
	//Handeling submission fo the general form
	//gets values of formfields
	@PostMapping("/settings/save_general1")
	public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
			HttpServletRequest request, RedirectAttributes ra) throws IOException {
	GeneralSettingBag settingBag = service.getGeneralSettings();
		
	saveSiteLogo(multipartFile, settingBag);
	saveCurrencySymbol(request, settingBag);
	updateSettingValuesFromForm(request, settingBag.list());
	
		ra.addFlashAttribute("message", "General settings have been saved.");
		
		return "redirect:/settings";
	}
	
	//Saving webpage logo
	private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename);
			//String fileName = multipartFile.getOriginalFilename;
			String value = "/site-logo/" + fileName;
			settingBag.updateSiteLogo(value);
			
			String uploadDir = "../site-logo/";
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir,  fileName, multipartFile);
		}
	}
	
	//Method for saving currency symbol
	private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) {
		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
        Optional <Currency> findByIdResult = currencyRepo.findById(currencyId);
        
        if (findByIdResult.isPresent()) {
        	Currency currency = findByIdResult.get();
        settingBag.updateCurrencySymbol(currency.getSymbol());
        }
        }
	
	//Reading values of the rest of the setting formfields
private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
	for (Setting setting : listSettings) {
	String value = request.getParameter(setting.getKey());
if(value != null){
	setting.setValue(value);
}
	}
	service.saveAll(listSettings);
}

//Handeling submission/saving of mail server form
@PostMapping("/settings/save_mail_server")
public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes ra) {
	List<Setting> mailServerSettings = service.getMailServerSettings();
	updateSettingValuesFromForm(request, mailServerSettings);
	
	ra.addFlashAttribute("message", "Mail server settings have been saved");
return "redirect:/settings";
}

//Handeling submission/saving of mail template settings form
@PostMapping("/settings/save_mail_templates")
public String saveMailTemplateSettings(HttpServletRequest request, RedirectAttributes ra) {
	List<Setting> mailTemplateSettings = service.getMailTemplateSettings();
	updateSettingValuesFromForm(request, mailTemplateSettings);
	
	ra.addFlashAttribute("message", "Mail template settings have been saved");
return "redirect:/settings";
}
}
