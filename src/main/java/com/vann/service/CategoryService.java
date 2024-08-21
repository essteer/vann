package com.vann.service;

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

    public Optional<Category> findCategoryById(UUID CategoryUuid) {
        return categoryRepo.findById(CategoryUuid);
    }

    public Category updateCategory(UUID categoryUuid, Category updatedCategory) {
        // Check if the Category exists
        if (!categoryRepo.existsById(categoryUuid)) {
            throw new IllegalArgumentException("Category not found");
        }
        // Set the ID of the updated Category
        updatedCategory.setCategoryUuid(categoryUuid);
        // Save the updated Category
        return categoryRepo.save(updatedCategory);
    }

    public void deleteCategory(UUID categoryUuid) {
        categoryRepo.deleteById(categoryUuid);
    }

}
