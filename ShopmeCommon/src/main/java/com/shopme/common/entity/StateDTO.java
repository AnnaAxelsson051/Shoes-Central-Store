package com.shopme.common.entity;

//Lightweight easily converted to Json, reducing network load

public class StateDTO {
	private Integer id;
	private String name;
	
	public StateDTO() {
		
	}
	
	

	public StateDTO(Integer id, String name) {
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
