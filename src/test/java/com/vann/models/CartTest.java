package com.vann.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.*;


public class CartTest {

    private Cart cart;
    private UUID customerId;
    private Map<UUID, Integer> items;

    @BeforeEach
    public void setUp() {
        customerId = UUID.randomUUID();
        items = new HashMap<>();
        UUID itemId1 = UUID.randomUUID();
        UUID itemId2 = UUID.randomUUID();
        items.put(itemId1, 0);
        items.put(itemId2, 0);
        cart = new Cart(customerId, items);
    }

    @Test
    public void testCartDefaultConstructor() {
        Cart defaultCart = new Cart();
        defaultCart.generateIdIfAbsent();
        assertNotNull(defaultCart.getId(), "UUID should be generated");
        assertNull(defaultCart.getCartCustomerId(), "CustomerId should be null");
        assertNotNull(defaultCart.getCartItems(), "Items should not be null");
        assertTrue(defaultCart.getCartItems().isEmpty(), "Items should be empty");
    }

    @Test
    public void testCartParameterizedConstructor() {
        assertNotNull(cart.getId(), "UUID should be generated");
        assertEquals(customerId, cart.getCartCustomerId(), "CustomerId should match");
        assertEquals(items, cart.getCartItems(), "Items should match");
        assertEquals(2, cart.getCartItems().size(), "Items size should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newId = UUID.randomUUID();
        cart.setId(newId);

        UUID newCustomerId = UUID.randomUUID();
        Map<UUID, Integer> newItems = new HashMap<>();
        UUID newItem1 = UUID.randomUUID();
        UUID newItem2 = UUID.randomUUID();
        newItems.put(newItem1, 1);
        newItems.put(newItem2, 1);

        cart.setCartCustomerId(newCustomerId);
        cart.setCartItems(newItems);

        assertEquals(newId, cart.getId(), "UUID should match");
        assertEquals(newCustomerId, cart.getCartCustomerId(), "CustomerId should match");
        assertEquals(newItems, cart.getCartItems(), "Items should match");
        assertEquals(2, cart.getCartItems().size(), "Items size should match");
    }

    @Test
    public void testToString() {
        String expectedString = "Cart [id=" + cart.getId() +
                ", customerId=" + customerId + "]";
        assertEquals(expectedString, cart.toString(), "toString should match the expected output");
    }
}
