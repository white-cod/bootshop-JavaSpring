package com.example.bootshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bootshop.models.Shoe;
import com.example.bootshop.service.ShoeService;

@Controller
public class ShoeController {

    @Autowired
    private ShoeService shoeService;

    @GetMapping("/")
    public String showCatalog(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "30") int size, 
        @RequestParam(required = false) Integer minPrice,
        @RequestParam(required = false) Integer maxPrice,
        Model model,
        Authentication authentication) {

        Page<Shoe> shoePage = shoeService.getShoesPaginated(page, size, minPrice, maxPrice);
        Integer minimumPrice = shoeService.getMinPrice();
        Integer maximumPrice = shoeService.getMaxPrice();

        model.addAttribute("minPrice", minimumPrice);
        model.addAttribute("maxPrice", maximumPrice);
        model.addAttribute("shoePage", shoePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", shoePage.getTotalPages());

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
        }

        return "catalog";
    }

    @GetMapping("/product/{id}")
    public String showProductDetails(@PathVariable("id") int id, Model model, Authentication authentication) {
        Shoe shoe = shoeService.findById(id); 

        if (shoe == null) {
            return "redirect:/";
        }
        model.addAttribute("shoe", shoe);

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }

        return "product-details";
    }
}