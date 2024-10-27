package com.example.bootshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bootshop.models.Shoe;
import com.example.bootshop.models.ShoppingCart;
import com.example.bootshop.models.User;
import com.example.bootshop.repository.ShoppingCartRepository;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoeService shoeService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShoppingCartRepository cartRepository;

    public List<ShoppingCart> getCartItems(User user) {
        return cartRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }


    public double getCartTotal(List<ShoppingCart> cartItems) {
        return cartItems.stream()
            .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
            .sum();
    }

    public void addToCart(Long userId, Integer shoeId, int quantity) {
        ShoppingCart cartItem = cartRepository.findByUserIdAndProductId(userId, shoeId);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            User user = userService.findById(userId);
            Shoe shoe = shoeService.findById(shoeId);
            cartItem = new ShoppingCart();
            cartItem.setUser(user);
            cartItem.setProduct(shoe);
            cartItem.setQuantity(quantity);
        }

        cartRepository.save(cartItem);
    }

    public void removeFromCart(Long userId, Integer productId) {
        ShoppingCart cartItem = cartRepository.findByUserIdAndProductId(userId, productId);
        if (cartItem != null) {
            cartRepository.delete(cartItem);
        }
    }


    public List<ShoppingCart> getAllCartItems() {
        return cartRepository.findAllCartItems();
    }

    public List<ShoppingCart> getCartItemsForUserDebug(Long userId) {
        return cartRepository.findCartItemsForUserDebug(userId);
    }
}