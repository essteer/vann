package com.vann.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import com.vann.models.enums.*;


public class ProductTest {

    private Product product;
    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category(CategoryType.RING, "Jewellery");
        product = new Product(category, "Ring", 499.99, "image.png", Size.US_07, Colour.YELLOW_GOLD, true);
    }

    @Test
    public void testProductDefaultConstructor() {
        Product defaultProduct = new Product();
        assertNull(defaultProduct.getCategory(), "Category should be null");
        assertNull(defaultProduct.getName(), "Product name should be null");
        assertEquals(0.0, defaultProduct.getPrice(), "Product price should be 0.0");
        assertNull(defaultProduct.getImageURI(), "Product image should be null");
        assertFalse(defaultProduct.getFeaturedStatus(), "Default featured status should be false");
    }

    @Test
    public void testProductParameterizedConstructor() {
        assertEquals(category, product.getCategory(), "Category ID should match");
        assertEquals("RING", product.getName(), "Product name should match");
        assertEquals(499.99, product.getPrice(), "Product price should match");
        assertEquals("image.png", product.getImageURI(), "Product imageURI should match");
        assertEquals(Size.US_07, product.getSize(), "Size should match");
        assertEquals(Colour.YELLOW_GOLD, product.getColour(), "Colour should match");
        assertTrue(product.getFeaturedStatus(), "Featured status should be true");
    }

    @Test
    public void testSettersAndGetters() {
        Category newCategory = new Category(CategoryType.NECKLACE, "Accessories");
        product.setCategory(newCategory);

        product.setName("Necklace");
        product.setPrice(299.99);
        product.setImageURI("necklace.png");
        product.setSize(Size.US_08);
        product.setColour(Colour.WHITE_GOLD);
        product.setFeaturedStatus(false);

        assertEquals(newCategory, product.getCategory(), "Category ID should match");
        assertEquals("NECKLACE", product.getName(), "Product name should match");
        assertEquals(299.99, product.getPrice(), "Product price should match");
        assertEquals("necklace.png", product.getImageURI(), "Product imageURI should match");
        assertEquals(Size.US_08, product.getSize(), "Size should match");
        assertEquals(Colour.WHITE_GOLD, product.getColour(), "Colour should match");
        assertEquals(false, product.getFeaturedStatus(), "Featured status should be false");
    }
    
}
