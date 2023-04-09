package com.shopme.admin.setting.country;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Country;

//Returning a list of countries sorted by name in asc order
public interface CountryRepository extends CrudRepository <Country, Integer >{
	public List<Country> findAllByOrderByNameAsc();
	
	

}
