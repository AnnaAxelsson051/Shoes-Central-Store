package com.shopme.setting;

import java.util.List;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingBag;

public class EmailSettingBag extends SettingBag{

	//Contructor
	public EmailSettingBag(List<Setting> listSettings) {
		super(listSettings);
	}
	
	//For reading the email settings
	public String getHost() {
		return super.getValue("MAIL_HOST");
	}
	

	public String getPort() {
		return super.getValue("MAIL_PORT");
	}

	public String getUsername() {
		return super.getValue("MAIL_USERNAME");
	}


	public String getPassword() {
		return super.getValue("MAIL_PASSWORD");
	}
	

	public String getSmtpAuth() {
		return super.getValue("SMTP_AUTH");
	}

	public String getSmtpSecured() {
		return super.getValue("SMTP_SECURED");
	}

	public String getFromAddress() {
		return super.getValue("MAIL_FROM");
	}
	

	public String getSenderName() {
		return super.getValue("MAIL_SENDER");
	}
	

	public String getCustomerVerifySubject() {
		return super.getValue("CUSTOMER_VERIFY_SUBJECT");
	}

	public String getCustomerVerifyContent() {
		return super.getValue("CUSTOMER_VERIFY_CONTENT");
	}

}
