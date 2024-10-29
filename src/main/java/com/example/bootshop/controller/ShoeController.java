package com.example.bootshop.controller;

import java.util.ArrayList;
import java.util.List;

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
        @RequestParam(defaultValue = "32") int size,
        @RequestParam(required = false) List<String> brands,
        @RequestParam(required = false) List<String> seasons,
        @RequestParam(required = false) Integer minPrice,
        @RequestParam(required = false) Integer maxPrice,
        Model model,
        Authentication authentication) {

        Page<Shoe> shoePage = shoeService.getShoesPaginated(page, size, brands, seasons, minPrice, maxPrice);
        Integer minimumPrice = shoeService.getMinPrice();
        Integer maximumPrice = shoeService.getMaxPrice();
        List<String> allBrands = shoeService.getAllBrands();
        List<String> allSeasons = shoeService.getAllSeasons();

        model.addAttribute("minPrice", minimumPrice);
        model.addAttribute("maxPrice", maximumPrice);
        model.addAttribute("shoePage", shoePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", shoePage.getTotalPages());
        model.addAttribute("allBrands", allBrands);
        model.addAttribute("allSeasons", allSeasons);
        model.addAttribute("selectedBrands", brands != null ? brands : new ArrayList<>());
        model.addAttribute("selectedSeasons", seasons != null ? seasons : new ArrayList<>());

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