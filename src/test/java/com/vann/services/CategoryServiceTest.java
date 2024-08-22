package com.vann.services;

import com.vann.model.Category;
import com.vann.model.enums.CategoryType;
import com.vann.repositories.CategoryRepo;
import com.vann.service.CategoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepo categoryRepo;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCategory() {
        Category category = new Category();
        category.setCategoryName("Earring");

        when(categoryRepo.save(category)).thenReturn(category);

        Category savedCategory = categoryService.saveCategory(category);

        assertNotNull(savedCategory);
        assertEquals("Earring", savedCategory.getCategoryName());
        verify(categoryRepo, times(1)).save(category);
    }

    @Test
    void testFindCategoryById() {
        UUID categoryId = UUID.randomUUID();
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName("Earring");

        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));

        Optional<Category> foundCategory = categoryService.findCategoryById(categoryId);

        assertTrue(foundCategory.isPresent());
        assertEquals("Earring", foundCategory.get().getCategoryName());
        verify(categoryRepo, times(1)).findById(categoryId);
    }

    @Test
    void testDeleteCategory() {
        UUID categoryId = UUID.randomUUID();

        doNothing().when(categoryRepo).deleteById(categoryId);

        categoryService.deleteCategory(categoryId);

        verify(categoryRepo, times(1)).deleteById(categoryId);
    }

    @Test
    void testFindAllCategories() {
        List<Category> categories = Arrays.asList(
            new Category(CategoryType.EARRING, "Earring"),
            new Category(CategoryType.NECKLACE, "Necklace")
        );

        when(categoryRepo.findAll()).thenReturn(categories);

        List<Category> foundCategories = categoryService.findAllCategories();

        assertNotNull(foundCategories);
        assertEquals(2, foundCategories.size());
        verify(categoryRepo, times(1)).findAll();
    }
}
