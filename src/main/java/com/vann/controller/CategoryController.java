package com.vann.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vann.exceptions.FieldConflictException;
import com.vann.exceptions.RecordNotFoundException;
import com.vann.model.Category;
import com.vann.model.enums.CategoryType;
import com.vann.service.CategoryService;
import com.vann.utils.LogHandler;


@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        try {
            List<Category> categories = categoryService.findAllCategories();
            if (categories.isEmpty()) {
                LogHandler.status204NoContent("GET", CategoryController.class, "getAllCategories()");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            LogHandler.status200OK("GET", CategoryController.class, "getAllCategories()");
            return ResponseEntity.ok(categories);
            
        } catch (Exception e) {
            LogHandler.status500InternalServerError("GET", CategoryController.class, "getAllCategories()", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/type/{categoryType}")
    public ResponseEntity<List<Category>> getCategoriesByType(@PathVariable CategoryType categoryType) {
        try {
            List<Category> categories = categoryService.findCategoriesByType(categoryType);
            if (categories.isEmpty()) {
                LogHandler.status204NoContent("GET", CategoryController.class, "getCategoriesByType()");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            LogHandler.status200OK("GET", CategoryController.class, "getCategoriesByType()");
            return ResponseEntity.ok(categories);
        
        } catch (IllegalArgumentException | MethodArgumentTypeMismatchException e) {
            LogHandler.status400BadRequest("GET", CategoryController.class, "getCategoriesByType()", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            LogHandler.status500InternalServerError("GET", CategoryController.class, "getCategoriesByType()", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable UUID id) {
        try {
            Category category = categoryService.findCategoryById(id);
            LogHandler.status200OK("GET", CategoryController.class, "getCategory()");
            return ResponseEntity.ok(category);
        } catch (RecordNotFoundException e) {
            LogHandler.status404NotFound("GET", CategoryController.class, "getCategory()", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("GET", CategoryController.class, "getCategory()", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<?> getCategoryByName(@PathVariable String categoryName) {
        try {
            Category category = categoryService.findCategoryByName(categoryName);
            return ResponseEntity.ok(category);
        } catch (RecordNotFoundException e) {
            LogHandler.status404NotFound("GET", CategoryController.class, "getCategoryByName()", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("GET", CategoryController.class, "getCategoryByName()", e.getMessage());
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        try {
            Category savedCategory = categoryService.saveCategory(category);
    
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCategory.getCategoryId())
                .toUri();

            LogHandler.status201Created("POST", CategoryController.class, "createCategory");
            return ResponseEntity.created(location).body(savedCategory);
        } catch (IllegalArgumentException | MethodArgumentTypeMismatchException e) {
            LogHandler.status400BadRequest("POST", CategoryController.class, "createCategory()", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (FieldConflictException e) {
            LogHandler.status409Conflict("POST", CategoryController.class, "createCategory()", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("POST", CategoryController.class, "createCategory()", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> createCategories(@RequestBody List<Category> categories) {
        try {
            List<Category> savedCategories = new ArrayList<>();
            for (Category category : categories) {
                try {
                    Category savedCategory = categoryService.saveCategory(category);
                    savedCategories.add(savedCategory);
                } catch (IllegalArgumentException | MethodArgumentTypeMismatchException e) {
                    LogHandler.status400BadRequest("POST", CategoryController.class, "createCategories()", e.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                } catch (FieldConflictException e) {
                    LogHandler.status409Conflict("POST", CategoryController.class, "createCategories()", e.getMessage());
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
                }
            }
            LogHandler.status201Created("POST", CategoryController.class, "createCategories");
            return ResponseEntity.status(HttpStatus.OK).body(savedCategories);
        } catch (Exception e) {
            LogHandler.status500InternalServerError("POST", CategoryController.class, "createCategories()", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable UUID id, @RequestBody Category updatedCategory) {
        try {
            Category updated = categoryService.updateCategory(id, updatedCategory);
            LogHandler.status200OK("PUT", CategoryController.class, "updateCategory()");
            return ResponseEntity.ok(updated);
        
        } catch (IllegalArgumentException | MethodArgumentTypeMismatchException e) {
            LogHandler.status400BadRequest("PUT", CategoryController.class, "updateCategory()", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RecordNotFoundException e) {
            LogHandler.status404NotFound("PUT", CategoryController.class, "updateCategory()", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (FieldConflictException e) {
            LogHandler.status409Conflict("PUT", CategoryController.class, "updateCategory()", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("PUT", CategoryController.class, "updateCategory()", e.getMessage());
            throw e;
        }
    }

}
