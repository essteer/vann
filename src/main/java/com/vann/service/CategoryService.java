package com.vann.service;

import com.vann.exceptions.CategoryNotFoundException;
import com.vann.model.Category;
import com.vann.model.enums.CategoryType;
import com.vann.repositories.CategoryRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public Category saveCategory(Category category) {
        return categoryRepo.save(category);
    }

    public List<Category> findAllCategories() {
        return categoryRepo.findAll();
    }

    public Category findCategoryById(UUID categoryId) {
        Optional<Category> categoryOptional = categoryRepo.findById(categoryId);
        return categoryOptional.orElseThrow(() -> 
            new CategoryNotFoundException("category with ID '" + categoryId + "'' not found"));
    }

    public List<Category> findCategoriesByType(CategoryType categoryType) {
        return categoryRepo.findByCategoryType(categoryType);
    }

    public Category findCategoryByName(String categoryName) {
        Optional<Category> categoryOptional = categoryRepo.findByCategoryName(categoryName);
        return categoryOptional.orElseThrow(() -> 
            new CategoryNotFoundException("category with name '" + categoryName + "'' not found"));
    }

    public Category updateCategory(UUID categoryId, Category updatedCategory) {
        if (!categoryRepo.existsById(categoryId)) {
            throw new CategoryNotFoundException("Category with ID '" + categoryId + "' not found");
        }
        updatedCategory.setCategoryId(categoryId);
        return categoryRepo.save(updatedCategory);
    }

    public void deleteCategory(UUID categoryId) {
        categoryRepo.deleteById(categoryId);
    }

}
