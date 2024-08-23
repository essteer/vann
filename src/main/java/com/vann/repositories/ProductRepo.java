package com.vann.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vann.model.Product;
import com.vann.model.enums.CategoryType;

@Repository
public interface ProductRepo extends JpaRepository<Product, UUID> {

    List<Product> findByCategory_CategoryName(String categoryName);
    List<Product> findByCategory_CategoryType(CategoryType categoryType);
    List<Product> findByProductName(String productName);

}
