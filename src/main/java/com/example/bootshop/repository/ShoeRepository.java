package com.example.bootshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bootshop.models.Shoe;

@Repository
public interface ShoeRepository extends JpaRepository<Shoe, Integer> {
    Page<Shoe> findAll(Pageable pageable);

    Optional<Shoe> getShoeById(int id);

    @Query("SELECT MIN(s.price) FROM Shoe s")
    Integer findMinPrice();

    @Query("SELECT MAX(s.price) FROM Shoe s")
    Integer findMaxPrice();

    @Query("SELECT s FROM Shoe s WHERE s.price BETWEEN :minPrice AND :maxPrice")
    Page<Shoe> findByPriceRange(@Param("minPrice") Integer minPrice, @Param("maxPrice") Integer maxPrice, Pageable pageable);

    @Query("SELECT DISTINCT s.brandName FROM Shoe s ORDER BY s.brandName ASC")
    List<String> findDistinctBrandNames();
}