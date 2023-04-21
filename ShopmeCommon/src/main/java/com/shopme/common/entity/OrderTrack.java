package com.shopme.common.entity;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "order_track")
public class OrderTrack extends IdBasedEntity{

	@Column(length = 256)
	private String notes;
	
	private Date updatedTime;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 45, nullable = false)
	private OrderStatus status;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	//Formatting date object into a string based on date time pattern
	//required by the html data input
	@Transient
	public String getUpdatedTimeOnForm() {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		return dateFormatter.format(this.updatedTime);
	}
	
	//Passing a string to a date object based on the pattern
	public void setUpdatedTimeOnForm(String dateString) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		
		try {
			this.updatedTime = dateFormatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
