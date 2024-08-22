package com.vann.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vann.model.Category;
import com.vann.model.enums.CategoryType;

@Repository
public interface CategoryRepo extends JpaRepository<Category, UUID> {
    
    List<Category> findByCategoryType(CategoryType categoryType);
    Optional<Category> findByCategoryName(String categoryName);

}
