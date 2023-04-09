package com.shopme.admin.setting.state;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

//Returning a list of states sorted by name in asc order
public interface StateRepository extends CrudRepository <State, Integer >{
	public List<State> findByCountryOrderByNameAsc(Country country);
}