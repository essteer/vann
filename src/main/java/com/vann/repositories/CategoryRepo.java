package com.vann.repositories;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vann.models.Category;
import com.vann.models.enums.CategoryType;

@Repository
public interface CategoryRepo extends JpaRepository<Category, UUID> {
    
    List<Category> findByCategoryType(CategoryType categoryType);
    Optional<Category> findByName(String name);

}
