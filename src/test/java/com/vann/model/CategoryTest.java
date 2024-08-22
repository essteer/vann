package com.vann.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vann.model.enums.CategoryType;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category(CategoryType.JEWELLERY, "Jewellery");
    }

    @Test
    public void testCategoryDefaultConstructor() {
        Category defaultCategory = new Category();
        defaultCategory.generateId();
        assertNotNull(defaultCategory.getCategoryId(), "UUID should be generated");
        assertNull(defaultCategory.getCategoryName(), "Category name should be null");
    }

    @Test
    public void testCategoryParameterizedConstructor() {
        assertNotNull(category.getCategoryId(), "UUID should be generated");
        assertEquals("Jewellery", category.getCategoryName(), "Category name should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newUuid = UUID.randomUUID();
        category.setCategoryId(newUuid);
        category.setCategoryName("Rings");

        assertEquals(newUuid, category.getCategoryId(), "UUID should match");
        assertEquals("Rings", category.getCategoryName(), "Category name should match");
    }

    @Test
    public void testToString() {
        String expectedString = "Category [id=" + category.getCategoryId() + ", type=" + category.getCategoryType() +
                                ", name=Jewellery]";
        assertEquals(expectedString, category.toString(), "toString should match");
    }
}