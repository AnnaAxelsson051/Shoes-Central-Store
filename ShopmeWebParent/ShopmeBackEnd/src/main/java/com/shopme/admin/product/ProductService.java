package com.shopme.admin.product;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Product;


@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repo;
	
	public List <Product> listAll(){
		return (List<Product>) repo.findAll();
	}
	
	//for saving a product when creating one
	//saving alias as productname where white spaces 
	//replaced by dashes
public Product save(Product product) {
	if(product.getId() == null) {
		product.setCreatedTime(new Date());
	}
	
	if(product.getAlias() == null || product.getAlias().isEmpty()) {
		String defaultAlias = product.getName().replaceAll(" ", "-");
	product.setAlias(defaultAlias);
	}else {
		product.setAlias(product.getAlias().replaceAll(" ", "-"));
	}
	product.setUpdatedTime(new Date());
	
	return repo.save(product);
}

//Checks if productname is unique when user is creating a product 
public String CheckUnique(Integer id, String name) {
	boolean isCreatingNew = (id == null|| id == 0);
	Product productByName = repo.findByName(name);
	
	if(isCreatingNew) {
		if(productByName != null) return "Duplicate";
	} else {
		if (productByName != null && productByName.getId() != id) {
			return "Duplicate";
		}
	}
	return "OK";
}

}
