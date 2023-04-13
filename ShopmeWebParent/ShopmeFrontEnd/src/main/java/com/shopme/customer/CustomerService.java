package com.shopme.customer;

import java.util.List;
import java.nio.charset.Charset;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.CountryRepository;
import com.shopme.common.entity.AuthenticationType;

import java.util.Random;


//import net.bytebuddy.utility.RandomString;

@Service
public class CustomerService {

	@Autowired 
	private CountryRepository countryRepo;
	
	@Autowired 
	private CustomerRepository customerRepo;
	
	@Autowired 
	PasswordEncoder passwordEncoder;
	
	//Listing all countries in form for customer to 
	//select from when creating account
	public List <Country> listAllCountries(){
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	//Checking if email user filled in to form is unique by 
	// or already in system
	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.findByEmail(email);
		return customer == null;
	}
	
	//Registering a new customer with a randomized verification code
	public void registerCustomer(Customer customer) {
		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(AuthenticationType.DATABASE);
		
		//String randomCode = RandomString.make(64);
		String randomCode = randomString(64);
		customer.setVerificationCode(randomCode);
		
		customerRepo.save(customer);
	}
	
	public static String randomString(int length) {
	    byte[] byteArray = new byte[length];
	    Random random = new Random();
	    random.nextBytes(byteArray);
	 
	    return new String(byteArray, Charset.forName("UTF-8"));
	}
	
	
	//Setting encoded pw to user obj
	private void encodePassword(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}
	
	//verifying cusomeraccount
	//getting customer by verificationcode and updates the enabled
	//status on that customer
	public boolean verify(String verificationCode) {
		Customer customer = customerRepo.findByVerificationCode(verificationCode);
	
		if(customer == null || customer.isEnabled()) {
			return false;	
	} else {
		customerRepo.enable(customer.getId());
		return true;
	}
		
	}
	
	public Customer getCustomerByEmail(String email) {
		return customerRepo.findByEmail(email);
	}
	
	//Checking if the auth type here is different than tha type in customer object then calling
	//customer repo to update the auth type
	public void updateAuthenticationType(Customer customer, AuthenticationType type) {
		if (!customer.getAuthenticationType().equals(type)) {
			customerRepo.updateAuthenticationType(customer.getId(), type);
		}
	}
	
	public void addNewCustomerUponOAuthLogin(String name, String email,
			String countryCode, AuthenticationType authenticationtype) {
		Customer customer = new Customer();
		customer.setEmail(email);
		setName(name, customer);
	
		customer.setEnabled(true);
		customer.setCreatedTime(new Date());
		customer.setAuthenticationType(authenticationtype);
		customer.setPassword("");
		customer.setAddressLine1("");
		customer.setCity("");
		customer.setState("");
		customer.setPhoneNumber("");
		customer.setPostalCode("");
		customer.setCountry(countryRepo.findByCode(countryCode));
		
		customerRepo.save(customer);
		}
	
	//If what customer entered (array) only contains one word set that to first name
	//Otherwise sets first name to the first element in array
	private void setName(String name, Customer customer) {
		String[] nameArray = name.split(" ");
		if(nameArray.length < 2) {
			customer.setFirstName(name);
			customer.setLastName("");
		}else {
			String firstName = nameArray[0];
			customer.setFirstName(firstName);
			
			String lastName = name.replaceFirst(firstName + " ", "");
			customer.setLastName(lastName);
		}
	}
	
	//Updating changes in user password only if the authentication type is
	//database, and copying the values of enabled, created time, verification code 
	//and pw (if not altered) from the customer in db object
	public void update (Customer customerInForm) {
		Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();
		
		if (customerInDB.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
		    if(!customerInForm.getPassword().isEmpty()) {
		    String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
		    customerInForm.setPassword(encodedPassword);
	        } else {
		    customerInForm.setPassword(customerInDB.getPassword());
	        }
		} else {
			customerInForm.setPassword(customerInDB.getPassword());
		}
		    customerInForm.setEnabled(customerInDB.isEnabled());
		    customerInForm.setCreatedTime(customerInDB.getCreatedTime());
		    customerInForm.setVerificationCode(customerInDB.getVerificationCode());
		    customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
		
	customerRepo.save(customerInForm);
}
}
