package com.shopme.address;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;

public interface AddressRepository extends CrudRepository<Address, Integer> {
	
	public List<Address> findByCustomer(Customer customer);
	
	@Query("SELECT a FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
	public Address findByIdAndCustomer(Integer addressId, Integer customerId);
	
	@Query("DELETE FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
	@Modifying
	public void deleteByIdAndCustomer(Integer addressId, Integer customerId);
	
	//Setting/selecting the default shipping address for an address id
	@Query("UPDATE Address a SET a.defaultForShipping = true WHERE a.id =?1")
	@Modifying
	public void setDefaultAddress(Integer id);
	
	//Setting address not to be default shipping 
	//selecting by address id and customer id
	@Query("UPDATE Address a SET a.defaultForShipping = false WHERE a.id =?1 "
			+ "AND a.customer.id = ?2")
	@Modifying
	public void setNonDefaultForOthers(Integer defaultAddressId, Integer customerId);

	//find customer default address with customer id
	@Query("SELECT a FROM Address a WHERE a.customer.id = ?1 AND a.defaultForShipping = true")
	public Address findDefaultByCustomer(Integer custoerId);
}