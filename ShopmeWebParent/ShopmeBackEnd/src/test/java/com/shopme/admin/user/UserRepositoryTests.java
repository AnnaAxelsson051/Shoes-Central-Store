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
	public void testCreateUser() {
		//creates table from user class
		Role roleAdmin = entityManager.find(Role.class, 1);  //Id 1 = admin
		User user1 = new User("test1@test1.com", "password", "test1FirstName", "test1LastName");
	user1.addRole(roleAdmin);
	
	User savedUser = repo.save(user1);
	assertThat(savedUser.getId()).isGreaterThan(0);
	}

}
