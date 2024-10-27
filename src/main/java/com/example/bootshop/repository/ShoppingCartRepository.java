package com.example.bootshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bootshop.models.ShoppingCart;
import com.example.bootshop.models.User;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {
    @Query("SELECT sc FROM ShoppingCart sc WHERE sc.user = ?1 ORDER BY sc.createdAt DESC")
    List<ShoppingCart> findByUserOrderByCreatedAtDesc(User user);
    
    @Query("SELECT sc FROM ShoppingCart sc WHERE sc.user.id = ?1 AND sc.product.id = ?2")
    ShoppingCart findByUserIdAndProductId(Long userId, int productId);

    @Query("SELECT sc FROM ShoppingCart sc JOIN FETCH sc.user JOIN FETCH sc.product")
    List<ShoppingCart> findAllCartItems();

    @Query("SELECT sc FROM ShoppingCart sc JOIN FETCH sc.user JOIN FETCH sc.product " +
           "WHERE sc.user.id = :userId")
    List<ShoppingCart> findCartItemsForUserDebug(@Param("userId") Long userId);

    @Query("SELECT sc FROM ShoppingCart sc WHERE sc.user.id = ?1 ORDER BY sc.createdAt DESC")
    List<ShoppingCart> findByUserIdOrderByCreatedAtDesc(Long userId);
}