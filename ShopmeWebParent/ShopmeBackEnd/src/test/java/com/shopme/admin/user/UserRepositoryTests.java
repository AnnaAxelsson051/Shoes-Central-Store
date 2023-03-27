package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace= Replace.NONE) //to run test on real db
@Rollback(false)  //for comitting changes to db after test 
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test 
	public void testCreateNewUserWithOneRole() {
		//creates table from user class
		Role roleAdmin = entityManager.find(Role.class, 1);  //Id 1 = admin
		User user1 = new User("test1@test1.com", "password", "test1FirstName", "test1LastName");
	user1.addRole(roleAdmin);
	
	User savedUser = repo.save(user1);
	assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test 
	public void testCreateNewUserWithTwoRoles() {
		//creates table from user class
		User user2 = new User("test1@test2.com", "password2", "test2FirstName", "test2LastName");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);

	user2.addRole(roleEditor);
	user2.addRole(roleAssistant);
	
	User savedUser = repo.save(user2);
	
	assertThat(savedUser.getId()).isGreaterThan(0);
	}

}
