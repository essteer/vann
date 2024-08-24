package com.vann.services;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.model.Cart;
import com.vann.model.CartItem;
import com.vann.repositories.CartRepo;
import com.vann.service.CartItemService;
import com.vann.service.CartService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepo cartRepo;

    @Mock
    private CartItemService cartItemService;

    @InjectMocks
    private CartService cartService;

    private UUID cartId;
    private UUID itemId;
    private Cart cart;
    private CartItem cartItem;

    @BeforeEach
    public void setUp() {
        cartId = UUID.randomUUID();
        itemId = UUID.randomUUID();
        cart = new Cart(UUID.randomUUID(), new HashSet<>());
        cartItem = new CartItem(itemId, 1);
    }

    @Test
    public void testFindAllCarts_ReturnsListOfCarts() {
        Cart cart1 = new Cart(UUID.randomUUID(), new HashSet<>());
        Cart cart2 = new Cart(UUID.randomUUID(), new HashSet<>());
        List<Cart> carts = Arrays.asList(cart1, cart2);

        when(cartRepo.findAll()).thenReturn(carts);

        List<Cart> result = cartService.findAllCarts();

        assertNotNull(result, "List of carts should not be null");
        assertEquals(2, result.size(), "List of carts should contain 2 items");
        assertTrue(result.contains(cart1), "List of carts should contain cart1");
        assertTrue(result.contains(cart2), "List of carts should contain cart2");
        verify(cartRepo).findAll();
    }

    @Test
    public void testFindCartById_FindsCartById() {
        Cart cart = new Cart(UUID.randomUUID(), new HashSet<>());

        when(cartRepo.findById(cartId)).thenReturn(Optional.of(cart));

        Cart result = cartService.findCartById(cartId);

        assertNotNull(result, "Cart should not be null");
        assertEquals(cart, result, "Returned cart should match the expected cart");
        verify(cartRepo).findById(cartId);
    }
    
    @Test
    public void testFindCartById_ThrowsExceptionWhenCartNotFound() {
        when(cartRepo.findById(cartId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> {
            cartService.findCartById(cartId);
        }, "Expected RecordNotFoundException to be thrown");

        verify(cartRepo).findById(cartId);
    }

    @Test
    public void testAddOrUpdateCartItems_AddsMultipleItems() {
        UUID productId = UUID.randomUUID();
        CartItem newItem = new CartItem(productId, 3);

        Map<UUID, Integer> items = Map.of(itemId, 3);

        when(cartRepo.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartItemService.findCartItemById(itemId)).thenReturn(newItem);
        when(cartRepo.save(any(Cart.class))).thenAnswer(invocation -> {
            Cart savedCart = invocation.getArgument(0);
            savedCart.getCartItems().add(newItem);
            return savedCart;
        });

        Cart result = cartService.addOrUpdateCartItems(cartId, items);

        assertNotNull(result, "Cart should not be null");
        assertTrue(result.getCartItems().contains(newItem), "Cart should contain the updated cart item");
        assertEquals(3, newItem.getQuantity(), "CartItem quantity should be 3");
        verify(cartRepo).findById(cartId);
        verify(cartItemService).findCartItemById(itemId);
        verify(cartRepo).save(cart);
    }

    @Test
    public void testAddOrUpdateCartItems_RemovesItemsWithZeroOrNegativeQuantity() {
        UUID productId = UUID.randomUUID();
        CartItem existingItem = new CartItem(productId, 5);
        cart.getCartItems().add(existingItem);

        Map<UUID, Integer> items = Map.of(itemId, 0);

        when(cartRepo.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartItemService.findCartItemById(itemId)).thenReturn(existingItem);
        when(cartRepo.save(any(Cart.class))).thenAnswer(invocation -> {
            Cart savedCart = invocation.getArgument(0);
            savedCart.getCartItems().removeIf(item -> item.getCartItemId().equals(itemId));
            return savedCart;
        });

        Cart result = cartService.addOrUpdateCartItems(cartId, items);

        assertNotNull(result, "Cart should not be null");
        assertFalse(result.getCartItems().stream()
            .anyMatch(item -> item.getCartItemId().equals(itemId)),
            "Cart should not contain the item with ID " + itemId + " after quantity updated to zero");
        verify(cartRepo).findById(cartId);
        verify(cartItemService).findCartItemById(itemId);
        verify(cartRepo).save(cart);
    }

    @Test
    public void testFindOrCreateCartByCustomerId_CreatesNewCart() {
        UUID customerId = UUID.randomUUID();
        Cart newCart = new Cart(customerId, new HashSet<>());

        when(cartRepo.findByCustomerId(customerId)).thenReturn(Optional.empty());
        when(cartRepo.save(any(Cart.class))).thenReturn(newCart);

        Cart result = cartService.findOrCreateCartByCustomerId(customerId);

        assertNotNull(result, "Cart should not be null");
        assertEquals(newCart, result, "Returned cart should be the new cart created");
        verify(cartRepo).findByCustomerId(customerId);
        verify(cartRepo).save(any(Cart.class));
    }

    @Test
    public void testFindOrCreateCartByCustomerId_ReturnsExistingCart() {
        UUID customerId = UUID.randomUUID();

        when(cartRepo.findByCustomerId(customerId)).thenReturn(Optional.of(cart));

        Cart result = cartService.findOrCreateCartByCustomerId(customerId);

        assertNotNull(result, "Cart should not be null");
        assertEquals(cart, result, "Returned cart should be the existing cart");
        verify(cartRepo).findByCustomerId(customerId);
        verifyNoMoreInteractions(cartRepo);
    }

    @Test
    public void testEmptyCart_RemovesAllItems() {
        cart.getCartItems().add(cartItem);
        when(cartRepo.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepo.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));  // Return the cart being saved

        Cart result = cartService.emptyCart(cartId);

        assertNotNull(result, "Cart should not be null");
        assertTrue(result.getCartItems().isEmpty(), "Cart should be empty");
        verify(cartRepo).findById(cartId);
        verify(cartItemService).deleteCartItem(cartItem.getCartItemId());
        verify(cartRepo).save(cart);
    }
}
