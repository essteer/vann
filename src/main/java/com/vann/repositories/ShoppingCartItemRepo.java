package com.vann.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vann.model.ShoppingCartItem;

public interface ShoppingCartItemRepo extends JpaRepository<ShoppingCartItem, UUID> {

}
