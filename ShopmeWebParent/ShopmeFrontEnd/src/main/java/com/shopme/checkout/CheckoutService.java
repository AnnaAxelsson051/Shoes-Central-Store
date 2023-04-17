package com.shopme.checkout;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.ShippingRate;

@Service
public class CheckoutService {
private static final int DIM_DIVISOR = 139;
	
	//Calculating product and shippingcost
	public CheckoutInfo prapareCheckout(List <CartItem> cartItems, ShippingRate shippingRate) {
		CheckoutInfo checkoutInfo = new CheckoutInfo();
		
		float productCost = calculateProductCost(cartItems); 
		float productTotal = calculateProductTotal(cartItems); 
		float shippingCostTotal = calculateShippingCost(cartItems, shippingRate); 
		float paymentTotal = productTotal + shippingCostTotal;
		
		checkoutInfo.setProductCost(productCost);
		checkoutInfo.setProductCost(productTotal);
		checkoutInfo.setShippingCostTotal(shippingCostTotal);
		checkoutInfo.setPaymentTotal(paymentTotal);
		checkoutInfo.setDeliverDays(shippingRate.getDays());
		checkoutInfo.setCodSupported(shippingRate.isCodSupported());
		
		
		
		return checkoutInfo;
	}
	
	
	//Calculating shipping cost based on dimensional weight
	//The weight out of product and dimweight that is larger is fassigned final weight
	//Displaying shipping cost for each product item on chech out page
	private float calculateShippingCost(List<CartItem> cartItems, ShippingRate shippingRate) {
		float shippingCostTotal = 0.0f;
		
		for (CartItem item : cartItems) {
			Product product = item.getProduct();
			float dimWeight = (product.getLength() * product.getWidth() * product.getHeight() / DIM_DIVISOR);
		    float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
		    float shippingCost = finalWeight * item.getQuantity() * shippingRate.getRate();
		item.setShippingCost(shippingCost);
		shippingCostTotal += shippingCost;
		
		}
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