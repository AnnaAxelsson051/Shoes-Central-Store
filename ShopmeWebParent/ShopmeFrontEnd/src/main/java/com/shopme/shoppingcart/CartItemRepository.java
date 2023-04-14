package com.shopme.shoppingcart;

import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.CartItem;

public interface CartItemRepository extends CrudRepository <CartItem, Integer> {

}
