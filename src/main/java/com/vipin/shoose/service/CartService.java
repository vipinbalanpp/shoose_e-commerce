package com.vipin.shoose.service;


import com.vipin.shoose.dto.SelectedProducts;
import com.vipin.shoose.model.Cart;
import com.vipin.shoose.model.User;
import com.vipin.shoose.model.Variant;

import java.util.List;
import java.util.Map;

public interface CartService {
    boolean addToCart(Long productId, String color, Long size);


    List<SelectedProducts> mapProductDetails();

    void removeItemFromCart(Long variantId);

    List<SelectedProducts> getProductsToCheckOut();

    void increaseQuantity(Long variantId);

    void decreaseQuantity(Long variantId);

    Float getTotalAmount(List<SelectedProducts> products);

    void clearCart();

    Map<Variant, Long> getProducts();

    void createCart(User user);
}
