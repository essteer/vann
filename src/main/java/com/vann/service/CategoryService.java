package com.vann.service;

import com.vann.exceptions.CategoryNotFoundException;
import com.vann.model.Category;
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

    public Optional<Category> findCategoryById(UUID CategoryId) {
        return categoryRepo.findById(CategoryId);
    }

    public Category updateCategory(UUID categoryId, Category updatedCategory) {
        if (!categoryRepo.existsById(categoryId)) {
            throw new CategoryNotFoundException("Category " + categoryId + " not found");
        }
        updatedCategory.setCategoryId(categoryId);
        return categoryRepo.save(updatedCategory);
    }

    public void deleteCategory(UUID categoryId) {
        categoryRepo.deleteById(categoryId);
    }

}
