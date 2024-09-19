package com.vann.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import com.vann.models.Category;
import com.vann.models.enums.CategoryType;
import com.vann.repositories.CategoryRepo;


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
        assertEquals("earring", savedCategory.getCategoryName());
        verify(categoryRepo, times(1)).save(category);
    }

    @Test
    void testFindAllCategories() {
        List<Category> categories = Arrays.asList(
            new Category(CategoryType.EARRING, "earring"),
            new Category(CategoryType.NECKLACE, "Necklace")
        );

        when(categoryRepo.findAll()).thenReturn(categories);

        List<Category> foundCategories = categoryService.findAllCategories();

        assertNotNull(foundCategories);
        assertEquals(2, foundCategories.size());
        verify(categoryRepo, times(1)).findAll();
    }

    @Test
    void testFindCategoryById() {
        UUID categoryId = UUID.randomUUID();
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName("earring");

        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));

        Category foundCategory = categoryService.findCategoryById(categoryId);

        assertEquals("earring", foundCategory.getCategoryName());
        verify(categoryRepo, times(1)).findById(categoryId);
    }

    @Test
    void testFindCategoriesByType() {
        CategoryType categoryType = CategoryType.EARRING;
        List<Category> categories = Arrays.asList(
            new Category(categoryType, "Gold Earring"),
            new Category(categoryType, "Silver Earring")
        );
    
        when(categoryRepo.findByCategoryType(categoryType)).thenReturn(categories);
    
        List<Category> foundCategories = categoryService.findCategoriesByType(categoryType);
    
        assertNotNull(foundCategories);
        assertEquals(2, foundCategories.size());
        assertEquals(categoryType, foundCategories.get(0).getCategoryType());
        verify(categoryRepo, times(1)).findByCategoryType(categoryType);
    }

    @Test
    void testFindCategoryByName() {
        String categoryName = "earring";
        Category category = new Category(CategoryType.EARRING, categoryName);
    
        when(categoryRepo.findByCategoryName(categoryName)).thenReturn(Optional.of(category));
        Category foundCategory = categoryService.findCategoryByName(categoryName);
    
        assertEquals(categoryName, foundCategory.getCategoryName());
        verify(categoryRepo, times(1)).findByCategoryName(categoryName);
    }

    @Test
    void testUpdateCategory() {
        UUID categoryId = UUID.randomUUID();
        Category existingCategory = new Category(CategoryType.EARRING, "earring");
        existingCategory.setCategoryId(categoryId);
    
        Category updatedCategory = new Category(CategoryType.NECKLACE, "necklace");
    
        when(categoryRepo.existsById(categoryId)).thenReturn(true);
        when(categoryRepo.save(updatedCategory)).thenReturn(updatedCategory);
    
        Category result = categoryService.updateCategory(categoryId, updatedCategory);
    
        assertNotNull(result);
        assertEquals(categoryId, result.getCategoryId());
        assertEquals("necklace", result.getCategoryName());
        verify(categoryRepo, times(1)).existsById(categoryId);
        verify(categoryRepo, times(1)).save(updatedCategory);
    }

    @Test
    void testDeleteCategory() {
        UUID categoryId = UUID.randomUUID();
        doNothing().when(categoryRepo).deleteById(categoryId);
        categoryService.deleteCategory(categoryId);
        verify(categoryRepo, times(1)).deleteById(categoryId);
    }

}
