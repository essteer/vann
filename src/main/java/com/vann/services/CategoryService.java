package com.vann.services;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vann.exceptions.*;
import com.vann.models.Category;
import com.vann.models.enums.CategoryType;
import com.vann.repositories.CategoryRepo;
import com.vann.utils.LogHandler;


@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Transactional
    public List<Category> createMany(List<Category> potentialCategories) {
        List<Category> savedCategories = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        for (Category potentialCategory : potentialCategories) {
            try {
                Category savedCategory = create(potentialCategory);
                savedCategories.add(savedCategory);
            } catch (IllegalArgumentException e) {
                errorMessages.add(e.getMessage());
            } catch (Exception e) {
                errorMessages.add(e.getMessage());
            }
        }

        if (errorMessages.isEmpty()) {
            LogHandler.status201Created(CategoryService.class + " | " + savedCategories.size() + " records created");
            return savedCategories;

        } else {
            throw new BulkOperationException(errorMessages);
        }
    }

    @Transactional
    public Category createOne(Category potentialCategory) {
        Category category = create(potentialCategory);
        LogHandler.status201Created(CategoryService.class + " | 1 record created");
        return category;
    }

    @Transactional
    private Category create(Category potentialCategory) {
        validateCategoryAttributes(potentialCategory);
        return saveCategory(potentialCategory);
    }

    private void validateCategoryAttributes(Category category) throws IllegalArgumentException {
        String name = category.getName();
        checkForNameConflict(category);
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(CategoryService.class + " | invalid name | cannot be null or empty");
        }
    
        CategoryType type = category.getType();
        if (type == null) {
            throw new IllegalArgumentException(CategoryService.class + " | invalid type | cannot be null");
        }
        if (!EnumSet.allOf(CategoryType.class).contains(type)) {
            throw new IllegalArgumentException(CategoryService.class + " | invalid type | type=" + type);
        }
    }
    
    private void checkForNameConflict(Category category) throws FieldConflictException {
        Optional<Category> existingCategoryOptional = categoryRepo.findByName(category.getName().toLowerCase());
    
        if (existingCategoryOptional.isPresent() && 
            !existingCategoryOptional.get().getId().equals(category.getId())) {
            throw new FieldConflictException(CategoryService.class + " | record already exists | name=" + category.getName());
        }
    }

    @Transactional
    public Category saveCategory(Category category) {
        return categoryRepo.save(category);
    }

    public List<Category> findAllCategories() {
        List<Category> categories = categoryRepo.findAll();
        logBulkFindOperation(categories, "findAll()");
        return categories;
    }

    public List<Category> findCategoriesByType(CategoryType categoryType) {
        List<Category> categories = categoryRepo.findByType(categoryType);
        logBulkFindOperation(categories, "type=" + categoryType);
        return categories;
    }

    private void logBulkFindOperation(List<Category> categories, String details) {
        if (categories.isEmpty()) {
            LogHandler.status204NoContent(CategoryService.class + " | 0 records | " + details);    
        } else {
            LogHandler.status200OK(CategoryService.class + " | " + categories.size() + " records | " + details);
        }
    }

    public Category findCategoryById(UUID id) throws RecordNotFoundException {
        Optional<Category> categoryOptional = categoryRepo.findById(id);

        if (categoryOptional.isPresent()) {
            LogHandler.status200OK(CategoryService.class + " | record found | id=" + id);
            return categoryOptional.get();
        } else {
            throw new RecordNotFoundException(CategoryService.class + " | record not found | id=" + id);
        }
    }

    public Category findCategoryByName(String name) throws RecordNotFoundException {
        Optional<Category> categoryOptional = categoryRepo.findByName(name);

        if (categoryOptional.isPresent()) {
            LogHandler.status200OK(CategoryService.class + " | record found | name=" + name);
            return categoryOptional.get();
        } else {
            throw new RecordNotFoundException(CategoryService.class + " | record not found | name=" + name);
        }
    }

    @Transactional
    public Category updateCategory(UUID id, Category updatedCategory) throws RecordNotFoundException {
        Category existingCategory = categoryRepo.findById(id).orElseThrow(() -> 
            new RecordNotFoundException(CategoryService.class + " | record not found | id=" + id)
        );

        validateCategoryAttributes(updatedCategory);
        existingCategory.setName(updatedCategory.getName());
        existingCategory.setType(updatedCategory.getType());
        Category savedCategory = saveCategory(existingCategory);
        LogHandler.status200OK(CategoryService.class + " | record updated | id=" + id);

        return savedCategory;
    }
    
    @Transactional
    public void deleteCategory(UUID id) {
        if (categoryRepo.existsById(id)) {
            categoryRepo.deleteById(id);
            LogHandler.status204NoContent(CategoryService.class + " | record deleted | id=" + id);
        }
    }

}
