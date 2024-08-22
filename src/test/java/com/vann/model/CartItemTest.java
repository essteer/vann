package com.vann.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vann.model.enums.CategoryType;
import com.vann.model.enums.Colour;
import com.vann.model.enums.Size;

import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CartItemTest {

    private CartItem cartItem;
    private Cart cart;
    private Product product;

    @BeforeEach
    public void setUp() {
        cart = new Cart(new Customer("John Doe", "john.doe@example.com"), new HashSet<>());
        product = new Product("Bracelet", 199.99, "bracelet.png", new Category(CategoryType.BRACELET, "Accessories"), Size.MEDIUM, Colour.SILVER);

        cartItem = new CartItem(cart, product, 3);
    }

    @Test
    public void testCartItemDefaultConstructor() {
        CartItem defaultItem = new CartItem();
        defaultItem.generateId();
        assertNotNull(defaultItem.getCartItemId(), "UUID should be generated");
        assertNull(defaultItem.getCart(), "Cart should be null");
        assertNull(defaultItem.getProduct(), "Product should be null");
        assertNull(defaultItem.getQuantity(), "Quantity should be null");
    }

    @Test
    public void testCartItemParameterizedConstructor() {
        assertNotNull(cartItem.getCartItemId(), "UUID should be generated");
        assertEquals(cart, cartItem.getCart(), "Cart should match");
        assertEquals(product, cartItem.getProduct(), "Product should match");
        assertEquals(3, cartItem.getQuantity(), "Quantity should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newUuid = UUID.randomUUID();
        cartItem.setCartItemId(newUuid);

        Cart newCart = new Cart(new Customer("Jane Doe", "jane.doe@example.com"), new HashSet<>());
        Product newProduct = new Product("Necklace", 299.99, "necklace.png", new Category(CategoryType.NECKLACE, "Jewellery"), Size.MEDIUM, Colour.SILVER);

        cartItem.setCart(newCart);
        cartItem.setProduct(newProduct);
        cartItem.setQuantity(5);

        assertEquals(newUuid, cartItem.getCartItemId(), "UUID should match");
        assertEquals(newCart, cartItem.getCart(), "Cart should match");
        assertEquals(newProduct, cartItem.getProduct(), "Product should match");
        assertEquals(5, cartItem.getQuantity(), "Quantity should match");
    }

    @Test
    public void testToString() {
        String expectedString = "CartItem [id=" + cartItem.getCartItemId() +
                ", cart=" + cart + 
                ", product=" + product + 
                ", quantity=" + cartItem.getQuantity() + "]";
        assertEquals(expectedString, cartItem.toString(), "toString should match the expected output");
    }
}
