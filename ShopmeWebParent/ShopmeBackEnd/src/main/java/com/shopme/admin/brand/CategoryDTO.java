package com.shopme.admin.brand;

//The object of this class is sent from server to client
//from BrandRestController
public class CategoryDTO {
	
	private Integer id;
	private String name;
	
	
	public CategoryDTO() {
	}
	public CategoryDTO(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
