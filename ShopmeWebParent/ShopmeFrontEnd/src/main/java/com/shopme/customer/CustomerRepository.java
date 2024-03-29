package com.shopme.customer;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

	@Query("SELECT c FROM Customer c WHERE c.email = ?1")
	public Customer findByEmail(String email);
	
	@Query("SELECT c FROM Customer c WHERE c.verificationCode = ?1")
	public Customer findByVerificationCode(String code);
	
	@Query("UPDATE Customer c SET c.enabled = true, "
			+ "and verificationCode = null WHERE c.id = ?1")
	@Modifying
	public void enable(Integer id);
	
	//Set auth type to value of sec param where c.id is the val of first param
	@Query("UPDATE Customer c SET c.authenticationType = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateAuthenticationType(Integer customerId, AuthenticationType type);

	//Finds user by the unique password token
	public Customer findByResetPasswordToken(String token);
}