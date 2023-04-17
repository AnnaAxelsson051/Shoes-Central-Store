package com.shopme.checkout;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.ShippingRate;

@Service
public class CheckoutService {

	
	//Calculating product and shippingcost
	public CheckoutInfo prapareCheckout(List <CartItem> cartItems, ShippingRate shippingRate) {
		CheckoutInfo checkoutInfo = new CheckoutInfo();
		
		float productCost = calculateProductCost(cartItems); 
		float productTotal = calculateProductTotal(cartItems); 
		float shippingCost = calculateShippingCost(cartItems, shippingRate); 
		
		checkoutInfo.setProductCost(productCost);
		checkoutInfo.setProductCost(productTotal);
		checkoutInfo.setDeliverDays(shippingRate.getDays());
		checkoutInfo.setCodSupported(shippingRate.isCodSupported());
		
		return checkoutInfo;
	}
	
	
	
	
	private float calculateShippingCost(List<CartItem> cartItems, ShippingRate shippingRate) {
		float shippingCostTotal = 0.0f;
		return shippingCostTotal;
	}

	private float calculateProductTotal(List<CartItem> cartItems) {
	float total = 0.0f;
		
		for (CartItem item : cartItems) {
			total += item.getSubTotal();
		}
		return total;
	}

	private float calculateProductCost(List<CartItem> cartItems) {
		float cost = 0.0f;
		
		for (CartItem item : cartItems) {
			cost += item.getQuantity() * item.getProduct().getCost();
		}
		return cost;
	}
}
