package com.vann.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vann.model.Cart;

@Repository
public interface CartRepo extends JpaRepository<Cart, UUID> {

}