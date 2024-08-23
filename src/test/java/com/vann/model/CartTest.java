package com.vann.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CartTest {

    private Cart cart;
    private Customer customer;
    private Set<CartItem> items;

    @BeforeEach
    public void setUp() {
        customer = new Customer("John Doe", "john.doe@example.com");
        items = new HashSet<>();
        CartItem item1 = new CartItem();
        CartItem item2 = new CartItem();
        items.add(item1);
        items.add(item2);

        cart = new Cart(customer, items);
    }

    @Test
    public void testCartDefaultConstructor() {
        Cart defaultCart = new Cart();
        defaultCart.generateIdIfAbsent();
        assertNotNull(defaultCart.getCartId(), "UUID should be generated");
        assertNull(defaultCart.getCartCustomer(), "Customer should be null");
        assertNotNull(defaultCart.getCartItems(), "Items should not be null");
        assertTrue(defaultCart.getCartItems().isEmpty(), "Items should be empty");
    }

    @Test
    public void testCartParameterizedConstructor() {
        assertNotNull(cart.getCartId(), "UUID should be generated");
        assertEquals(customer, cart.getCartCustomer(), "Customer should match");
        assertEquals(items, cart.getCartItems(), "Items should match");
        assertEquals(2, cart.getCartItems().size(), "Items size should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newId = UUID.randomUUID();
        cart.setCartId(newId);

        Customer newCustomer = new Customer("Jane Doe", "jane.doe@example.com");
        Set<CartItem> newItems = new HashSet<>();
        CartItem newItem = new CartItem();
        newItems.add(newItem);

        cart.setCartCustomer(newCustomer);
        cart.setCartItems(newItems);

        assertEquals(newId, cart.getCartId(), "UUID should match");
        assertEquals(newCustomer, cart.getCartCustomer(), "Customer should match");
        assertEquals(newItems, cart.getCartItems(), "Items should match");
        assertEquals(1, cart.getCartItems().size(), "Items size should match");
    }

    @Test
    public void testToString() {
        String expectedString = "Cart [id=" + cart.getCartId() +
                ", customer=" + customer + "]";
        assertEquals(expectedString, cart.toString(), "toString should match the expected output");
    }
}
