package com.shopme.admin.category;

import java.awt.print.Pageable;
import java.util.*;

//import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

import jakarta.transaction.Transactional;

@Service 
@Transactional
public class CategoryService {

	//page size = 4 toplevel cats per page
	private static final int ROOT_CATEGORIES_PER_PAGE = 4;
		@Autowired
		private CategoryRepository repo;
		

		//categories listed sorted asc by default or toggled by user
		public List<Category> listByPage(CategoryPageInfo pageInfo, 
				int pageNum, String sortDir) {
			Sort sort = Sort.by("name");
			
			if(sortDir.equals("asc")) { 
				sort = sort.ascending();
			} else if (sortDir.equals("desc")) {
				sort = sort.descending();
			}
			
		 Pageable pageable = PageRequest.of(pageNum -1, ROOT_CATEGORIES_PER_PAGE, sort);
			
		 Page<Category> pageCategories = repo.findRootCategories(pageable);
		 List<Category> rootCategories = pageCategories.getContent();
		 
		 pageInfo.setTotalElements(pageCategories.getTotalElements());
		 pageInfo.setTotalPages(pageCategories.getTotalPages());
		 
		 return listHierarchicalCategories(rootCategories, sortDir);
		}
		
		
		
		
		private List <Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir){
			List <Category> hierarchicalCategories = new ArrayList<>();;
			
			for(Category rootCategory : rootCategories) {
				hierarchicalCategories.add(Category.copyFull(rootCategory));
			
			Set<Category> children = sortSubCategories(rootCategory.getChildren(),sortDir);
			
			for(Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierarchicalCategories.add(Category.copyFull(subCategory, name));
			
			listSubHierarchicalCategories(hierarchicalCategories, subCategory, 1, sortDir);
			}
			}
			
			return hierarchicalCategories;
		}
		
		private void listSubHierarchicalCategories(List<Category> 
		hierarchicalCategories, Category parent, int subLevel, String sortDir) {
			Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
			int newSubLevel = subLevel +1;
			
			for(Category subCategory : children) {
				String name = "";
				for (int i = 0; i < newSubLevel; i++) {
					name += "--";
				}
				name += subCategory.getName();
				
				hierarchicalCategories.add(Category.copyFull(subCategory,name));
			
				listSubHierarchicalCategories(hierarchicalCategories, subCategory, 
						newSubLevel, sortDir);
			}
		}
		
		
		public Category save(Category category) {
			return repo.save(category);
		}
		
		
		
		//making categories appear hierarchical in form to select from
		public List <Category> listCategoriesUsedInForm(){
			List<Category> categoriesUsedInForm = new ArrayList<>();
			
			Iterable<Category> categoriesInDB = repo.findRootCategories(Sort.by("name").ascending());
				
				for(Category category : categoriesInDB) {
					//if (category.getParent() == null) {
						categoriesUsedInForm.add(Category.copyIdAndName(category));
						
						Set<Category> children = sortSubCategories(category.getChildren());
						for(Category subCategory : children) {
							String name = "--" + subCategory.getName();
					    categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(),name));
						
					    listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, 1);
						//}
					}
				}
			
			return categoriesUsedInForm;
		}
		
		//making children of categories appear in form to select from
		private void listSubCategoriesUsedInForm(List <Category> categoriesUsedInForm, 
				Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = sortSubCategories(parent.getChildren());
		
		for(Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			
			categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(),name));
			
			listSubCategoriesUsedInForm(categoriesUsedInForm, subCategory, newSubLevel);
		}
	}
		
		public Category get(Integer id) throws CategoryNotFoundException{
			try {
				return repo.findById(id).get();
			}catch (NoSuchElementException ex) {
				throw new CategoryNotFoundException("Could not find any category with id " + id);
			}
		}
	
//Cheks so that newly created category name not already in system
		public String checkUnique(Integer id, String name, String alias) {
			boolean isCreatingNew = (id == null || id == 0);
			
			Category categoryByName = repo.findByName(name);
			
			if(isCreatingNew) {
				if (categoryByName != null) {
					return "DuplicateName";
				}else {
					Category categoryByAlias = repo.findByAlias(alias);
					if(categoryByAlias != null) {
						return "DuplicateAlias";
					}
				}
			}else {
				if(categoryByName != null && categoryByName.getId() != id) {
					return "DuplicateName";
				}
				Category categoryByAlias = repo.findByAlias(alias);
				if (categoryByAlias != null && categoryByAlias.getId() != id ) {
					return "DuplicateAlias";
			  }
			}
			return "OK";
			}
		
		//Used by method that retuns list om cat used in form 
		private SortedSet<Category> sortSubCategories(Set<Category> children){
		return sortSubCategories(children, "asc");
		}
		
		//Subcategories sorted in asch/desc
		private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir){
			SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
			
			@Override
			public int compare(Category cat1, Category cat2) {
				if(sortDir.equals("asc")) {
					return cat1.getName().compareTo(cat2.getName());
				}else {
					return cat2.getName().compareTo(cat1.getName());
				}
			}
			});
			
			sortedChildren.addAll(children);
			
			return sortedChildren;
		}
		
		public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
			repo.updateEnabledStatus(id, enabled);
		}
	
		
	//Deleting categories if cat id is found
		public void delete(Integer id) throws CategoryNotFoundException{
			Long countById = repo.countById(id);
			if (countById == null || countById == 0) {
				throw new CategoryNotFoundException("Could not find category with id " + id);
				
			}
			repo.deleteById(id);
		}
	}


