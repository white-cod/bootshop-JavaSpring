package com.example.bootshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.bootshop.models.Shoe;
import com.example.bootshop.repository.ShoeRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ShoeRepository shoeRepository;

    @GetMapping("")
    public String adminDashboard(Model model) {
        List<Shoe> shoes = shoeRepository.findAll();
        model.addAttribute("shoes", shoes);
        return "admin/dashboard";
    }
}