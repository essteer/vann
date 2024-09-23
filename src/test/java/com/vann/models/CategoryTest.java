package com.vann.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import com.vann.models.enums.CategoryType;


public class CategoryTest {

    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category(CategoryType.JEWELLERY, "jewellery");
    }

    @Test
    public void testCategoryDefaultConstructor() {
        Category defaultCategory = new Category();
        assertNull(defaultCategory.getName(), "Category name should be null");
        assertNull(defaultCategory.getType(), "Category type should be null");
    }

    @Test
    public void testCategoryParameterizedConstructor() {
        assertEquals("jewellery", category.getName(), "Category name should match");
        assertEquals(CategoryType.JEWELLERY, category.getType(), "Category type should match");
    }

    @Test
    public void testSettersAndGetters() {
        category.setName("rings");
        category.setType(CategoryType.RING);
        
        assertEquals("rings", category.getName(), "Category name should match");
        assertEquals(CategoryType.RING, category.getType(), "Category type should match");
    }

    @Test
    public void testToString() {
        String expectedString = "Category [id=" + category.getId() + ", type=" + category.getType() +
                                ", name=jewellery]";
        assertEquals(expectedString, category.toString(), "toString should match");
    }
}