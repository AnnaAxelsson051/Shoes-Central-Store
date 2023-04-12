package com.shopme.admin.user;


import org.springframework.data.domain.Pageable; 
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.User;

//For pagination function
//public interface UserRepository extends SearchRepository <User, Integer>{
public interface UserRepository extends SearchRepository <User, Integer>, CrudRepository <User, Integer>, PagingAndSortingRepository <User, Integer>{

	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User getUserByEmail(@Param("email") String email);
	
	public Long countById(Integer id); //method from spring data jpa no sql query needed

	//Searching for user with spec name or email
	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName) LIKE %?1%") //=keyword
	public Page <User> findAll(String keyword, Pageable pageable);

	//setting u.enabled prop to the value of second param in method where user id is eq to thefirst param 
	@Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
	@Modifying
	public void  updateEnabledStatus(Integer id, boolean enabled);
}
