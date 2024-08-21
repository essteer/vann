package com.vann.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingCartTest {

    private ShoppingCart shoppingCart;
    private Customer customer;
    private Set<ShoppingCartItem> items;

    @BeforeEach
    public void setUp() {
        customer = new Customer("John Doe", "john.doe@example.com");
        items = new HashSet<>();
        ShoppingCartItem item1 = new ShoppingCartItem();
        ShoppingCartItem item2 = new ShoppingCartItem();
        items.add(item1);
        items.add(item2);

        shoppingCart = new ShoppingCart(customer, items);
    }

    @Test
    public void testShoppingCartDefaultConstructor() {
        ShoppingCart defaultCart = new ShoppingCart();
        assertNotNull(defaultCart.getShoppingCartUuid(), "UUID should be generated");
        assertNull(defaultCart.getCustomer(), "Customer should be null");
        assertNotNull(defaultCart.getItems(), "Items should not be null");
        assertTrue(defaultCart.getItems().isEmpty(), "Items should be empty");
    }

    @Test
    public void testShoppingCartParameterizedConstructor() {
        assertNotNull(shoppingCart.getShoppingCartUuid(), "UUID should be generated");
        assertEquals(customer, shoppingCart.getCustomer(), "Customer should match");
        assertEquals(items, shoppingCart.getItems(), "Items should match");
        assertEquals(2, shoppingCart.getItems().size(), "Items size should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newUuid = UUID.randomUUID();
        shoppingCart.setShoppingCartUuid(newUuid);

        Customer newCustomer = new Customer("Jane Doe", "jane.doe@example.com");
        Set<ShoppingCartItem> newItems = new HashSet<>();
        ShoppingCartItem newItem = new ShoppingCartItem();
        newItems.add(newItem);

        shoppingCart.setCustomer(newCustomer);
        shoppingCart.setItems(newItems);

        assertEquals(newUuid, shoppingCart.getShoppingCartUuid(), "UUID should match");
        assertEquals(newCustomer, shoppingCart.getCustomer(), "Customer should match");
        assertEquals(newItems, shoppingCart.getItems(), "Items should match");
        assertEquals(1, shoppingCart.getItems().size(), "Items size should match");
    }

    @Test
    public void testToString() {
        String expectedString = "ShoppingCart [shoppingCartUuid=" + shoppingCart.getShoppingCartUuid() +
                ", customer=" + customer + ", items=" + items + "]";
        assertEquals(expectedString, shoppingCart.toString(), "toString should match the expected output");
    }
}
