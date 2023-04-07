package com.shopme.category;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
public class CategoryService {
	
	@Autowired private CategoryRepository repo;
	
	//Iterates tru enabled cats and checks if cat has children and
	//puts it into list
	public List <Category> listNoChildrenCategories(){
		List<Category> listNoChildrenCategories = new ArrayList<>();
	List<Category> listEnabledCategories = repo.findAllEnabled();
	listEnabledCategories.forEach(category ->{
		Set<Category> children = category.getChildren();
		if(children == null ||children.size() == 0) {
			listNoChildrenCategories.add(category);
		}
	});
	return listNoChildrenCategories;
	}
	
	//Getting a (enabled) category by alias 
	public Category getCategory (String alias) {
		return repo.findByAliasEnabled(alias);
	}

}
