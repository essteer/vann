package com.vann.models;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;

import java.util.*;


public class CartTest {

    private Cart cart;
    private User user;
    private UUID userId;
    private Map<UUID, Integer> items;

    @BeforeEach
    public void setUp() {
        user = mock(User.class);
        items = new HashMap<>();
        UUID itemId1 = UUID.randomUUID();
        UUID itemId2 = UUID.randomUUID();
        items.put(itemId1, 0);
        items.put(itemId2, 0);
        cart = new Cart(user, items);
    }

    @Test
    public void testCartDefaultConstructor() {
        Cart defaultCart = new Cart();
        assertNotNull(defaultCart.getCartItems(), "Items should not be null");
        assertTrue(defaultCart.getCartItems().isEmpty(), "Items should be empty");
    }

    @Test
    public void testCartParameterizedConstructor() {
        assertEquals(items, cart.getCartItems(), "Items should match");
        assertEquals(2, cart.getCartItems().size(), "Items size should match");
    }

    @Test
    public void testSettersAndGetters() {
        Map<UUID, Integer> newItems = new HashMap<>();
        UUID newItem1 = UUID.randomUUID();
        UUID newItem2 = UUID.randomUUID();
        newItems.put(newItem1, 1);
        newItems.put(newItem2, 1);

        cart.setCartItems(newItems);

        assertEquals(newItems, cart.getCartItems(), "Items should match");
        assertEquals(2, cart.getCartItems().size(), "Items size should match");
    }

    @Test
    public void testToString() {
        String expectedString = "Cart [id=" + cart.getId() +
                ", userId=" + userId + "]";
        assertEquals(expectedString, cart.toString(), "toString should match the expected output");
    }
}
