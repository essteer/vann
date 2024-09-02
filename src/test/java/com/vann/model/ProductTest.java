package com.vann.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vann.model.enums.CategoryType;
import com.vann.model.enums.Colour;
import com.vann.model.enums.Size;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private Product product;
    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category(CategoryType.RING, "Jewellery");
        product = new Product(category.getCategoryId(), "Ring", 499.99, "image.png", Size.US_07, Colour.YELLOW_GOLD);
    }

    @Test
    public void testProductDefaultConstructor() {
        Product defaultProduct = new Product();
        defaultProduct.generateIdIfAbsent();
        assertNotNull(defaultProduct.getProductId(), "UUID should be generated");
        assertNull(defaultProduct.getCategoryId(), "Category ID should be null");
        assertNull(defaultProduct.getProductName(), "Product name should be null");
        assertEquals(0.0, defaultProduct.getProductPrice(), "Product price should be 0.0");
        assertNull(defaultProduct.getProductImage(), "Product image should be null");
    }

    @Test
    public void testProductParameterizedConstructor() {
        assertNotNull(product.getProductId(), "UUID should be generated");
        assertEquals(category.getCategoryId(), product.getCategoryId(), "Category ID should match");
        assertEquals("RING", product.getProductName(), "Product name should match");
        assertEquals(499.99, product.getProductPrice(), "Product price should match");
        assertEquals("image.png", product.getProductImage(), "Product image should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newUuid = UUID.randomUUID();
        product.setProductId(newUuid);

        Category newCategory = new Category(CategoryType.NECKLACE, "Accessories");
        product.setCategoryId(newCategory.getCategoryId());

        product.setProductName("Necklace");
        product.setProductPrice(299.99);
        product.setProductImage("necklace.png");

        assertEquals(newUuid, product.getProductId(), "UUID should match");
        assertEquals(newCategory.getCategoryId(), product.getCategoryId(), "Category ID should match");
        assertEquals("NECKLACE", product.getProductName(), "Product name should match");
        assertEquals(299.99, product.getProductPrice(), "Product price should match");
        assertEquals("necklace.png", product.getProductImage(), "Product image should match");
    }

    @Test
    public void testToString() {
        String expectedString = "Product [id=" + product.getProductId() +
                ", categoryId=" + category.getCategoryId() + ", name=RING, unitprice=499.99, size=" + 
                product.getSize() + ", colour=" + product.getColour() + "]";

        assertEquals(expectedString, product.toString(), "toString should match the expected output");
    }
}
