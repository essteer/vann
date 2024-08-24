package com.vann.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CartItemTest {

    private CartItem cartItem;
    private UUID productId;

    @BeforeEach
    public void setUp() {
        productId = UUID.randomUUID();
        cartItem = new CartItem(productId, 3);
    }

    @Test
    public void testCartItemDefaultConstructor() {
        CartItem defaultItem = new CartItem();
        defaultItem.generateIdIfAbsent();
        assertNotNull(defaultItem.getCartItemId(), "UUID should be generated");
        assertNull(defaultItem.getProductId(), "Product ID should be null");
        assertNull(defaultItem.getQuantity(), "Quantity should be null");
    }

    @Test
    public void testCartItemParameterizedConstructor() {
        assertNotNull(cartItem.getCartItemId(), "UUID should be generated");
        assertEquals(productId, cartItem.getProductId(), "Product ID should match");
        assertEquals(3, cartItem.getQuantity(), "Quantity should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newUuid = UUID.randomUUID();
        UUID newProductId = UUID.randomUUID();
        cartItem.setCartItemId(newUuid);
        cartItem.setProductId(newProductId);
        cartItem.setQuantity(5);

        assertEquals(newUuid, cartItem.getCartItemId(), "UUID should match");
        assertEquals(newProductId, cartItem.getProductId(), "Product ID should match");
        assertEquals(5, cartItem.getQuantity(), "Quantity should match");
    }

    @Test
    public void testToString() {
        String expectedString = "CartItem [id=" + cartItem.getCartItemId() +
                ", productId=" + productId + 
                ", quantity=" + cartItem.getQuantity() + "]";
        assertEquals(expectedString, cartItem.toString(), "toString should match the expected output");
    }
}
