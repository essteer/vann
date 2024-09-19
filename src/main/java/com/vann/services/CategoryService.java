package com.vann.services;

import java.util.*;
import org.springframework.stereotype.Service;

import com.vann.exceptions.*;
import com.vann.models.Category;
import com.vann.models.enums.CategoryType;
import com.vann.repositories.CategoryRepo;


@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<Category> findAllCategories() {
        return categoryRepo.findAll();
    }

    public List<Category> findCategoriesByType(CategoryType categoryType) {
        return categoryRepo.findByType(categoryType);
    }

    public Category findCategoryById(UUID categoryId) throws RecordNotFoundException {
        Optional<Category> categoryOptional = categoryRepo.findById(categoryId);
        return categoryOptional.orElseThrow(() -> 
            new RecordNotFoundException("Category with ID '" + categoryId + "'' not found"));
    }

    public Category findCategoryByName(String categoryName) throws RecordNotFoundException {
        Optional<Category> categoryOptional = categoryRepo.findByName(categoryName);
        return categoryOptional.orElseThrow(() -> 
            new RecordNotFoundException("Category with name '" + categoryName + "'' not found"));
    }

    public Category saveCategory(Category category) {
        checkForNameConflict(category);
        category.generateIdIfAbsent();
        return categoryRepo.save(category);
    }
    
    public Category updateCategory(UUID categoryId, Category updatedCategory) throws RecordNotFoundException {
        if (!categoryRepo.existsById(categoryId)) {
            throw new RecordNotFoundException("Category with ID '" + categoryId + "' not found");
        }
        updatedCategory.setId(categoryId);
        updatedCategory.setName(updatedCategory.getName());
        return saveCategory(updatedCategory);
    }
    
    private void checkForNameConflict(Category category) throws FieldConflictException {
        Optional<Category> existingCategoryOptional = categoryRepo.findByName(category.getName().toLowerCase());
    
        if (existingCategoryOptional.isPresent() && 
            !existingCategoryOptional.get().getId().equals(category.getId())) {
            throw new FieldConflictException("Category with name '" + category.getName() + "' already exists");
        }
    }

    public void deleteCategory(UUID categoryId) {
        categoryRepo.deleteById(categoryId);
    }

}
