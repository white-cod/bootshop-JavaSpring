package com.example.bootshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.bootshop.models.Shoe;
import com.example.bootshop.repository.ShoeRepository;

@Service
public class ShoeService {

    @Autowired
    private ShoeRepository shoeRepository;

    public Page<Shoe> getShoesPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return shoeRepository.findAll(pageable);
    }

    public Shoe findById(int id) {
        return shoeRepository.findById(id).orElse(null);
    }

    public Shoe getShoeById(int id) {
        return shoeRepository.findById(id).orElse(null);
    }

    
    public Integer getMinPrice() {
        return shoeRepository.findMinPrice();
    }

    public Integer getMaxPrice() {
        return shoeRepository.findMaxPrice();
    }

    public Page<Shoe> getShoesPaginated(int page, int size, Integer minPrice, Integer maxPrice) {
        Pageable pageable = PageRequest.of(page, size);
        if (minPrice != null && maxPrice != null) {
            return shoeRepository.findByPriceRange(minPrice, maxPrice, pageable);
        }
        return shoeRepository.findAll(pageable);
    }

    public List<String> getDistinctBrandNames() {
        return shoeRepository.findDistinctBrandNames();
    }

}