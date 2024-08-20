package com.vann.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vann.model.Product;

public interface ProductRepo extends JpaRepository<Product, UUID> {

}
