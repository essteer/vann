package com.vann.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vann.model.CartItem;

public interface CartItemRepo extends JpaRepository<CartItem, UUID> {

}
