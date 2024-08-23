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
        category = new Category(CategoryType.JEWELLERY, "jewellery");
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
        assertEquals("jewellery", category.getCategoryName(), "Category name should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newUuid = UUID.randomUUID();
        category.setCategoryId(newUuid);
        category.setCategoryName("rings");

        assertEquals(newUuid, category.getCategoryId(), "UUID should match");
        assertEquals("rings", category.getCategoryName(), "Category name should match");
    }

    @Test
    public void testToString() {
        String expectedString = "Category [id=" + category.getCategoryId() + ", type=" + category.getCategoryType() +
                                ", name=jewellery]";
        assertEquals(expectedString, category.toString(), "toString should match");
    }
}