package com.vann.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.model.CartItem;
import com.vann.repositories.CartItemRepo;
import com.vann.service.CartItemService;

@SpringBootTest
public class CartItemServiceTest {

    @MockBean
    private CartItemRepo cartItemRepo;

    @Autowired
    private CartItemService cartItemService;

    private UUID cartItemId;
    private UUID productId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cartItemId = UUID.randomUUID();
        productId = UUID.randomUUID();
    }

    @Test
    public void testCreateCartItem() {
        int quantity = 1;
        CartItem expectedCartItem = new CartItem(productId, quantity);
        expectedCartItem.setCartItemId(cartItemId);

        when(cartItemRepo.save(any(CartItem.class))).thenReturn(expectedCartItem);

        CartItem result = cartItemService.createCartItem(productId, quantity);

        assertNotNull(result, "CartItem should not be null");
        assertEquals(expectedCartItem.getProductId(), result.getProductId(), "Product ID should match");
        assertEquals(expectedCartItem.getQuantity(), result.getQuantity(), "Quantity should match");
        verify(cartItemRepo).save(any(CartItem.class));
    }

    @Test
    public void testFindAllCartItems() {
        List<CartItem> cartItems = List.of(
            new CartItem(UUID.randomUUID(), 2),
            new CartItem(UUID.randomUUID(), 3)
        );
        when(cartItemRepo.findAll()).thenReturn(cartItems);

        List<CartItem> result = cartItemService.findAllCartItems();

        assertNotNull(result, "Resulting list should not be null");
        assertEquals(2, result.size(), "Resulting list should have 2 items");
        verify(cartItemRepo).findAll();
    }

    @Test
    public void testFindCartItemById() {
        CartItem expectedCartItem = new CartItem(productId, 1);
        expectedCartItem.setCartItemId(cartItemId);
        when(cartItemRepo.findById(cartItemId)).thenReturn(Optional.of(expectedCartItem));

        CartItem result = cartItemService.findCartItemById(cartItemId);

        assertNotNull(result, "CartItem should not be null");
        assertEquals(expectedCartItem, result, "Returned CartItem should match");
        verify(cartItemRepo).findById(cartItemId);
    }

    @Test
    public void testFindCartItemById_ThrowsExceptionWhenNotFound() {
        when(cartItemRepo.findById(cartItemId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> {
            cartItemService.findCartItemById(cartItemId);
        }, "Expected RecordNotFoundException to be thrown");

        verify(cartItemRepo).findById(cartItemId);
    }

    @Test
    public void testUpdateCartItem() {
        CartItem updatedItem = new CartItem(productId, 5);
        updatedItem.setCartItemId(cartItemId);

        when(cartItemRepo.existsById(cartItemId)).thenReturn(true);
        when(cartItemRepo.save(any(CartItem.class))).thenReturn(updatedItem);

        CartItem result = cartItemService.updateCartItem(cartItemId, updatedItem);

        assertNotNull(result, "CartItem should not be null");
        assertEquals(updatedItem, result, "Returned CartItem should match the updated CartItem");
        verify(cartItemRepo).existsById(cartItemId);
        verify(cartItemRepo).save(any(CartItem.class));
    }

    @Test
    public void testUpdateCartItem_ThrowsExceptionWhenNotFound() {
        CartItem updatedCartItem = new CartItem(productId, 3);

        when(cartItemRepo.existsById(cartItemId)).thenReturn(false);

        assertThrows(RecordNotFoundException.class, () -> {
            cartItemService.updateCartItem(cartItemId, updatedCartItem);
        }, "Expected RecordNotFoundException to be thrown");

        verify(cartItemRepo).existsById(cartItemId);
    }

    @Test
    public void testDeleteCartItem() {
        when(cartItemRepo.existsById(cartItemId)).thenReturn(true);
        cartItemService.deleteCartItem(cartItemId);
        verify(cartItemRepo).existsById(cartItemId);
        verify(cartItemRepo).deleteById(cartItemId);
    }

    @Test
    public void testDeleteCartItem_ThrowsExceptionWhenNotFound() {
        when(cartItemRepo.existsById(cartItemId)).thenReturn(false);

        assertThrows(RecordNotFoundException.class, () -> {
            cartItemService.deleteCartItem(cartItemId);
        }, "Expected RecordNotFoundException to be thrown");

        verify(cartItemRepo).existsById(cartItemId);
    }
}