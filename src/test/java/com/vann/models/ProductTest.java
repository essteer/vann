package com.vann.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import com.vann.models.enums.*;


public class ProductTest {

    private Product product;
    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category(CategoryType.RING, "Jewellery");
        product = new Product(category, "Ring", 499.99, "image.png", Size.US_07, Colour.YELLOW_GOLD);
    }

    @Test
    public void testProductDefaultConstructor() {
        Product defaultProduct = new Product();
        defaultProduct.generateIdIfAbsent();
        assertNotNull(defaultProduct.getId(), "UUID should be generated");
        assertNull(defaultProduct.getCategory(), "Category should be null");
        assertNull(defaultProduct.getName(), "Product name should be null");
        assertEquals(0.0, defaultProduct.getPrice(), "Product price should be 0.0");
        assertNull(defaultProduct.getImageURI(), "Product image should be null");
    }

    @Test
    public void testProductParameterizedConstructor() {
        assertNotNull(product.getId(), "UUID should be generated");
        assertEquals(category, product.getCategory(), "Category ID should match");
        assertEquals("RING", product.getName(), "Product name should match");
        assertEquals(499.99, product.getPrice(), "Product price should match");
        assertEquals("image.png", product.getImageURI(), "Product image should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newUuid = UUID.randomUUID();
        product.setId(newUuid);

        Category newCategory = new Category(CategoryType.NECKLACE, "Accessories");
        product.setCategory(newCategory);

        product.setName("Necklace");
        product.setPrice(299.99);
        product.setImageURI("necklace.png");

        assertEquals(newUuid, product.getId(), "UUID should match");
        assertEquals(newCategory, product.getCategory(), "Category ID should match");
        assertEquals("NECKLACE", product.getName(), "Product name should match");
        assertEquals(299.99, product.getPrice(), "Product price should match");
        assertEquals("necklace.png", product.getImageURI(), "Product image should match");
    }

    @Test
    public void testToString() {
        String expectedString = "Product [id=" + product.getId() +
                ", category_name=" + category.getName() + ", name=RING, unitprice=499.99, size=" + 
                product.getSize() + ", colour=" + product.getColour() + "]";

        assertEquals(expectedString, product.toString(), "toString should match the expected output");
    }
}
