package com.vann.repositories;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vann.models.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, UUID> {

    List<Product> findByCategoryId(UUID categoryId);
    List<Product> findByName(String name);
    List<Product> findByFeaturedStatus(Boolean featuredStatus);

}
