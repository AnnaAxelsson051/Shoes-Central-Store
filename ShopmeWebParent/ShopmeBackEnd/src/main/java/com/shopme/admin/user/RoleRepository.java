package com.shopme.admin.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Role;

//Exteds interface frovided by spring data
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>{  //entity type, type of id field

}
