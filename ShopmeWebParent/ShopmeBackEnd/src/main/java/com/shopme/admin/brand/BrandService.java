package com.shopme.admin.brand;

/*import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Brand;*/
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import com.shopme.admin.paging.PagingAndSortingHelper;
//import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Brand;

@Service
public class BrandService {
	
	public static final int BRANDS_PER_PAGE = 10;
	@Autowired
	private BrandRepository repo;
	
	public List<Brand> listAll(){
		return (List<Brand>) repo.findAll();
	}
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper){
		Sort sort = Sort.by(helper.getSortField());
		
		sort = helper.getSortDir().equals("asc") ? sort.ascending() : sort.descending();
	Pageable pageable = PageRequest.of(pageNum - 1,  BRANDS_PER_PAGE, sort);
	Page<Brand> page = null;
	
	if(helper.getKeyword() != null) {
		page = repo.findAll(helper.getKeyword(),  pageable);
		} else {
	   page = repo.findAll(pageable);
		}
	helper.updateModelAttributes(pageNum,  page);
	}
	
	
	public Brand save (Brand brand) {
		return repo.save(brand);
	}
	
	public Brand get(Integer id) throws BrandNotFoundException{
		try {
			return repo.findById(id).get();
		}catch (NoSuchElementException ex) {
			throw new BrandNotFoundException ("Could not find any brand with id " + id);		
			}
	}
	
	public void delete(Integer id) throws BrandNotFoundException{
		Long countById = repo.countById(id);
		
		if(countById == null || countById == 0) {
			throw new BrandNotFoundException("Could not find any brand with id " + id);
		}
		repo.deleteById(id);
		}
	
	//Check if a brand name is unique and if not prevents from creating similar
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Brand brandByName = repo.findByName(name);
		
		if(isCreatingNew) {
			if (brandByName != null) return "Duplicate";
		}else {
			if (brandByName != null && brandByName.getId() != id) {
				return "Duplicate";
			}
		}
		return "OK";
	}
	}


