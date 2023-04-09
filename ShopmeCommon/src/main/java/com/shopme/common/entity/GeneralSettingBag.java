package com.shopme.common.entity;

import java.util.List;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingBag;

//For updating site logo and currency symbol
public class GeneralSettingBag extends SettingBag {

	public GeneralSettingBag(List<Setting> listSettings) {
		super(listSettings);
	}

	public void updateCurrencySymbol(String value) {
		super.update("CURRENCY_SYMBOL", value);
	}
	
	public void updateSiteLogo(String value) {
		super.update("SITE_LOGO", value);
	}
}