package com.shopme.shoppingcart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import com.shopme.product.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ShoppingCartService {
	
	@Autowired 
	private CartItemRepository cartRepo;
	
	@Autowired 
	private ProductRepository productRepo;

	//Adding product to cart or updating quantity
	
	public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {
		Integer updatedQuantity = quantity;
		Product product = new Product(productId);
		
		CartItem cartItem = cartRepo.findByCustomerAndProduct(customer, product);
	
	if(cartItem != null) {
		updatedQuantity = cartItem.getQuantity() + quantity;

		if(updatedQuantity > 5) {
			throw new ShoppingCartException("Could not add more " + quantity + " item(s) "
					+ "because there is already " + cartItem.getQuantity() + " item(s) in your shopping cart. "
							+ "Maximum allowed quantity is 5.");
		}
	} else {
		cartItem = new CartItem();
		cartItem.setCustomer(customer);
		cartItem.setProduct(product);
		
	}
	cartItem.setQuantity(updatedQuantity);
	
	cartRepo.save(cartItem);
	
	return updatedQuantity;
	}
	
	//Listing what is in cart 
	public List <CartItem> listCartItems(Customer customer){
		return cartRepo.findByCustomer(customer);
	}
	
	//Returning subtotal of cart
	public float updateQuantity(Integer productId, Integer quantity, Customer customer) {
		cartRepo.updateQuantity(quantity, customer.getId(), productId);
	Product product = productRepo.findById(productId).get();
	float subtotal = product.getDiscountPrice() * quantity;
	return subtotal;
	}
	
	//Removing product from cart
	public void removeProduct(Integer productId, Customer customer) {
	cartRepo.deleteByCustomerAndProduct(customer.getId(), productId);	
	}
	
	//Emptying cart when customer checks out order
	public void deleteByCustomer(Customer customer) {
		cartRepo.deleteByCustomer(customer.getId());
	}
	
}
