package com.vann.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vann.model.enums.Colour;
import com.vann.model.enums.Size;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private Product product;
    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category("Jewellery", "Luxury jewellery items");
        product = new Product("Ring", "Gold ring with diamonds", 499.99, "Available", "image.png", category, Size.US_07, Colour.YELLOW_GOLD);
    }

    @Test
    public void testProductDefaultConstructor() {
        Product defaultProduct = new Product();
        assertNotNull(defaultProduct.getProductUuid(), "UUID should be generated");
        assertNull(defaultProduct.getProductName(), "Product name should be null");
        assertNull(defaultProduct.getProductDesc(), "Product description should be null");
        assertEquals(0.0, defaultProduct.getProductPrice(), "Product price should be 0.0");
        assertNull(defaultProduct.getProductStatus(), "Product status should be null");
        assertNull(defaultProduct.getProductImage(), "Product image should be null");
        assertNull(defaultProduct.getCategory(), "Category should be null");
    }

    @Test
    public void testProductParameterizedConstructor() {
        assertNotNull(product.getProductUuid(), "UUID should be generated");
        assertEquals("Ring", product.getProductName(), "Product name should match");
        assertEquals("Gold ring with diamonds", product.getProductDesc(), "Product description should match");
        assertEquals(499.99, product.getProductPrice(), "Product price should match");
        assertEquals("Available", product.getProductStatus(), "Product status should match");
        assertEquals("image.png", product.getProductImage(), "Product image should match");
        assertEquals(category, product.getCategory(), "Category should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newUuid = UUID.randomUUID();
        product.setProductUuid(newUuid);
        product.setProductName("Necklace");
        product.setProductDesc("Silver necklace with emeralds");
        product.setProductPrice(299.99);
        product.setProductStatus("Out of Stock");
        product.setProductImage("necklace.png");

        Category newCategory = new Category("Accessories", "Luxury accessories");
        product.setCategory(newCategory);

        assertEquals(newUuid, product.getProductUuid(), "UUID should match");
        assertEquals("Necklace", product.getProductName(), "Product name should match");
        assertEquals("Silver necklace with emeralds", product.getProductDesc(), "Product description should match");
        assertEquals(299.99, product.getProductPrice(), "Product price should match");
        assertEquals("Out of Stock", product.getProductStatus(), "Product status should match");
        assertEquals("necklace.png", product.getProductImage(), "Product image should match");
        assertEquals(newCategory, product.getCategory(), "Category should match");
    }

    @Test
    public void testToString() {
        String expectedString = "Product [productUuid=" + product.getProductUuid() +
                ", productName=Ring, productDesc=Gold ring with diamonds, productPrice=499.99, productStatus=Available, category=" +
                category.toString() + ", size=" + product.getSize() + ", colour=" + product.getColour() + "]";

        assertEquals(expectedString, product.toString(), "toString should match the expected output");
    }
}
