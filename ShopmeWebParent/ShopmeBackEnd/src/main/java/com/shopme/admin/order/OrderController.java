package com.shopme.admin.order;


import java.util.List;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.setting.SettingService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.OrderTrack;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.setting.Setting;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;


@Controller
public class OrderController {
	private String defaultRedirectURL = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";
	
	@Autowired private OrderService orderService;
	@Autowired private SettingService settingService;

	@GetMapping("/orders")
	public String listFirstPage() {
		return defaultRedirectURL;
	}
	
	//Listing orders by page
	//Updating amount based on currncy settings
	@GetMapping("/orders/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listOrders", moduleURL = "/orders") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum,
			HttpServletRequest request) {

		orderService.listByPage(pageNum, helper);
		loadCurrencySetting(request);
		
		return "orders/orders";
	}
	
	
	private void loadCurrencySetting(HttpServletRequest request) {
		List<Setting> currencySettings = settingService.getCurrencySettings();
		
		for (Setting setting : currencySettings) {
			request.setAttribute(setting.getKey(), setting.getValue());
		}	
	}	
	
	//Allowing for view of order details
	//Getting order object loading currency settings putting order onto model
	@GetMapping("/orders/detail/{id}")
	public String viewOrderDetails(@PathVariable("id") Integer id, Model model, 
			RedirectAttributes ra, HttpServletRequest request) {
		try {
			Order order = orderService.get(id);
			loadCurrencySetting(request);			
			model.addAttribute("order", order);
			
			return "orders/order_details_modal";
		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}
		
	}
	
	//Deleting order
	//Calling deletemethod in service setting successmessage
	@GetMapping("/orders/delete/{id}")
	public String deleteOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			orderService.delete(id);;
			ra.addFlashAttribute("message", "The order ID " + id + " has been deleted.");
		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
		
		return defaultRedirectURL;
	}
	
	
	//Getting an order obj based on order id, ad a list of countries sorted in asc order
	//Displaying a list of countries in the order_form.html
	@GetMapping("/orders/edit/{id}")
	public String editOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes ra,
			HttpServletRequest request) {
		try {
			Order order = orderService.get(id);;
			
			List<Country> listCountries = orderService.listAllCountries();
			
			model.addAttribute("pageTitle", "Edit Order (ID: " + id + ")");
			model.addAttribute("order", order);
			model.addAttribute("listCountries", listCountries);
			
			return "orders/order_form";
			
		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}
	}
	
		
		@PostMapping("/order/save")
		public String saveOrder(Order order, HttpServletRequest request, RedirectAttributes ra) {
			String countryName = request.getParameter("countryName");
			order.setCountry(countryName);
			
			updateProductDetails(order, request);
			updateOrderTracks(order, request);
			
			orderService.save(order);
			ra.addFlashAttribute("message", "The order ID " + order.getId() + " has been updated successfully.");
			return defaultRedirectURL;
		}

		//Returning a list collection of order tracks
		private void updateOrderTracks(Order order, HttpServletRequest request) {
		String[] trackIds = request.getParameterValues("trackId");
		String[] trackStatuses = request.getParameterValues("trackStatus");
		String[] trackDates = request.getParameterValues("trackDate");
		String[] trackNotes = request.getParameterValues("trackNotes");
		
		List <OrderTrack> orderTracks = order.getOrderTracks();
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		
		for (int i = 0; i < trackIds.length; i++) {
			OrderTrack trackRecord = new OrderTrack();
			
			Integer trackId = Integer.parseInt(trackIds[i]);
			if (trackId > 0) {
				trackRecord.setId(trackId);
			}
			
			trackRecord.setOrder(order);
			trackRecord.setStatus(OrderStatus.valueOf(trackStatuses[i]));
			trackRecord.setNotes(trackNotes[i]);
			try {
				trackRecord.setUpdatedTime(dateFormatter.parse(trackDates[i]));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			orderTracks.add(trackRecord);
		}
			
		}

		//Putting the input from order_form_add_products.html into string arrays
		//Iterating tru each element
		private void updateProductDetails(Order order, HttpServletRequest request) {
			String[] detailIds = request.getParameterValues("detailId");
			String[] productIds = request.getParameterValues("productId");
			String[] productPrices = request.getParameterValues("productPrice");
			String[] productDetailCosts = request.getParameterValues("productDetailCost");
			String[] quantities = request.getParameterValues("quantity");
			String[] productSubtotals = request.getParameterValues("productSubtotal");
			String[] productShipCosts = request.getParameterValues("productShipcost");
			
			Set<OrderDetail> orderDetails = order.getOrderDetails();
			
			for (int i = 0; i < detailIds.length; i++) {
				System.out.println("Detail ID: " + detailIds[i]);
				System.out.println("\t Product ID " + productIds[i]);
				System.out.println("\t Cost: " + productDetailCosts[i]);
				System.out.println("\t Quantity: " + quantities[i]);
				System.out.println("\t Subtotal: " + productSubtotals[i]);
				System.out.println("\t Ship cost: " + productShipCosts[i]);
			
				OrderDetail orderDetail = new OrderDetail();
				Integer detailId = Integer.parseInt(detailIds[i]);
				if (detailId > 0) {
					orderDetail.setId(detailId);
				}
				orderDetail.setOrder(order);
				orderDetail.setProduct(new Product(Integer.parseInt(productIds[i])));
				orderDetail.setProductCost(Float.parseFloat(productDetailCosts[i]));
				orderDetail.setSubtotal(Float.parseFloat(productSubtotals[i]));
				orderDetail.setShippingCost(Float.parseFloat(productShipCosts[i]));
				orderDetail.setQuantity(Integer.parseInt(quantities[i]));
				orderDetail.setUnitPrice(Float.parseFloat(productPrices[i]));
			
				orderDetails.add(orderDetail);
			}
		
		}
		
	}


