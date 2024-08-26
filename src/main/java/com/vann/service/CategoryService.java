package com.vann.service;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.exceptions.FieldConflictException;
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

    public List<Category> findAllCategories() {
        return categoryRepo.findAll();
    }

    public List<Category> findCategoriesByType(CategoryType categoryType) {
        return categoryRepo.findByCategoryType(categoryType);
    }

    public Category findCategoryById(UUID categoryId) throws RecordNotFoundException {
        Optional<Category> categoryOptional = categoryRepo.findById(categoryId);
        return categoryOptional.orElseThrow(() -> 
            new RecordNotFoundException("Category with ID '" + categoryId + "'' not found"));
    }

    public Category findCategoryByName(String categoryName) throws RecordNotFoundException {
        Optional<Category> categoryOptional = categoryRepo.findByCategoryName(categoryName);
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
        updatedCategory.setCategoryId(categoryId);
        updatedCategory.setCategoryName(updatedCategory.getCategoryName());
        return saveCategory(updatedCategory);
    }
    
    private void checkForNameConflict(Category category) throws FieldConflictException {
        Optional<Category> existingCategoryOptional = categoryRepo.findByCategoryName(category.getCategoryName().toLowerCase());
    
        if (existingCategoryOptional.isPresent() && 
            !existingCategoryOptional.get().getCategoryId().equals(category.getCategoryId())) {
            throw new FieldConflictException("Category with name '" + category.getCategoryName() + "' already exists");
        }
    }

    public void deleteCategory(UUID categoryId) {
        categoryRepo.deleteById(categoryId);
    }

}
