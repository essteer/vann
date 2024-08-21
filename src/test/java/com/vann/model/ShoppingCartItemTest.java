package com.vann.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingCartItemTest {

    private ShoppingCartItem shoppingCartItem;
    private ShoppingCart shoppingCart;
    private Product product;

    @BeforeEach
    public void setUp() {
        shoppingCart = new ShoppingCart(new Customer("John Doe", "john.doe@example.com"), new HashSet<>());
        product = new Product("Bracelet", "Silver bracelet", 199.99, "Available", "bracelet.png", new Category("Accessories", "Luxury items"));

        shoppingCartItem = new ShoppingCartItem(shoppingCart, product, 3);
    }

    @Test
    public void testShoppingCartItemDefaultConstructor() {
        ShoppingCartItem defaultItem = new ShoppingCartItem();
        assertNotNull(defaultItem.getItemUuid(), "UUID should be generated");
        assertNull(defaultItem.getShoppingCart(), "ShoppingCart should be null");
        assertNull(defaultItem.getProduct(), "Product should be null");
        assertNull(defaultItem.getQuantity(), "Quantity should be null");
    }

    @Test
    public void testShoppingCartItemParameterizedConstructor() {
        assertNotNull(shoppingCartItem.getItemUuid(), "UUID should be generated");
        assertEquals(shoppingCart, shoppingCartItem.getShoppingCart(), "ShoppingCart should match");
        assertEquals(product, shoppingCartItem.getProduct(), "Product should match");
        assertEquals(3, shoppingCartItem.getQuantity(), "Quantity should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newUuid = UUID.randomUUID();
        shoppingCartItem.setItemUuid(newUuid);

        ShoppingCart newCart = new ShoppingCart(new Customer("Jane Doe", "jane.doe@example.com"), new HashSet<>());
        Product newProduct = new Product("Necklace", "Gold necklace", 299.99, "Available", "necklace.png", new Category("Jewelry", "Luxury items"));

        shoppingCartItem.setShoppingCart(newCart);
        shoppingCartItem.setProduct(newProduct);
        shoppingCartItem.setQuantity(5);

        assertEquals(newUuid, shoppingCartItem.getItemUuid(), "UUID should match");
        assertEquals(newCart, shoppingCartItem.getShoppingCart(), "ShoppingCart should match");
        assertEquals(newProduct, shoppingCartItem.getProduct(), "Product should match");
        assertEquals(5, shoppingCartItem.getQuantity(), "Quantity should match");
    }

    @Test
    public void testToString() {
        String expectedString = "ShoppingCartItem [itemUuid=" + shoppingCartItem.getItemUuid() +
                ", shoppingCart=" + shoppingCart + 
                ", product=" + product + 
                ", quantity=" + shoppingCartItem.getQuantity() + "]";
        assertEquals(expectedString, shoppingCartItem.toString(), "toString should match the expected output");
    }
}
