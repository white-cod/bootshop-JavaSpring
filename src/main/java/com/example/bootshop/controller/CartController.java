package com.example.bootshop.controller;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.bootshop.models.Shoe;
import com.example.bootshop.models.ShoppingCart;
import com.example.bootshop.models.User;
import com.example.bootshop.service.ShoeService;
import com.example.bootshop.service.ShoppingCartService;
import com.example.bootshop.service.UserService;

import ch.qos.logback.classic.Logger;

@Controller
public class CartController {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(CartController.class);

    @Autowired
    private ShoppingCartService cartService;

    @GetMapping("/cart")
    public String viewCart(Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String userEmail = authentication.getName();
        model.addAttribute("userEmail", userEmail);

        User user = userService.findByEmail(userEmail);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/";
        }

        List<ShoppingCart> cartItems = cartService.getCartItems(user);
        double total = cartService.getCartTotal(cartItems);

        model.addAttribute("userId", user.getId());
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);

        return "cart";
    }


    @Autowired
    private ShoeService shoeService;

    @Autowired
    private UserService userService;

    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable("id") int shoeId, RedirectAttributes redirectAttributes, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("userEmail", userEmail);
        User user = userService.findByEmail(userEmail); 

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/";
        }

        Shoe shoe = shoeService.findById(shoeId);

        if (shoe == null) {
            redirectAttributes.addFlashAttribute("error", "Product not found");
            return "redirect:/";
        }

        cartService.addToCart(user.getId(), shoeId, 1);
        redirectAttributes.addFlashAttribute("success", "Product added to cart");

        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable("id") int productId, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String userEmail = authentication.getName();
        User user = userService.findByEmail(userEmail);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/";
        }

        try {
            cartService.removeFromCart(user.getId(), productId);
            redirectAttributes.addFlashAttribute("success", "Item removed from cart");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error removing item from cart");
        }

        return "redirect:/cart";
    }
}