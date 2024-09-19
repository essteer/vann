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
        category.setName("Earring");

        when(categoryRepo.save(category)).thenReturn(category);

        Category savedCategory = categoryService.saveCategory(category);

        assertNotNull(savedCategory);
        assertEquals("earring", savedCategory.getName());
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
        UUID id = UUID.randomUUID();
        Category category = new Category();
        category.setId(id);
        category.setName("earring");

        when(categoryRepo.findById(id)).thenReturn(Optional.of(category));

        Category foundCategory = categoryService.findCategoryById(id);

        assertEquals("earring", foundCategory.getName());
        verify(categoryRepo, times(1)).findById(id);
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
        assertEquals(categoryType, foundCategories.get(0).getType());
        verify(categoryRepo, times(1)).findByCategoryType(categoryType);
    }

    @Test
    void testFindCategoryByName() {
        String name = "earring";
        Category category = new Category(CategoryType.EARRING, name);
    
        when(categoryRepo.findByName(name)).thenReturn(Optional.of(category));
        Category foundCategory = categoryService.findCategoryByName(name);
    
        assertEquals(name, foundCategory.getName());
        verify(categoryRepo, times(1)).findByName(name);
    }

    @Test
    void testUpdateCategory() {
        UUID id = UUID.randomUUID();
        Category existingCategory = new Category(CategoryType.EARRING, "earring");
        existingCategory.setId(id);
    
        Category updatedCategory = new Category(CategoryType.NECKLACE, "necklace");
    
        when(categoryRepo.existsById(id)).thenReturn(true);
        when(categoryRepo.save(updatedCategory)).thenReturn(updatedCategory);
    
        Category result = categoryService.updateCategory(id, updatedCategory);
    
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("necklace", result.getName());
        verify(categoryRepo, times(1)).existsById(id);
        verify(categoryRepo, times(1)).save(updatedCategory);
    }

    @Test
    void testDeleteCategory() {
        UUID id = UUID.randomUUID();
        doNothing().when(categoryRepo).deleteById(id);
        categoryService.deleteCategory(id);
        verify(categoryRepo, times(1)).deleteById(id);
    }

}
