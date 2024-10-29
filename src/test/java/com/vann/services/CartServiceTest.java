package com.vann.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import com.vann.exceptions.*;
import com.vann.models.*;
import com.vann.repositories.*;


public class CartServiceTest {

    @Mock
    private CartRepo cartRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    private Cart cart;
    private User user;
    private UUID cartId;
    private UUID userId;
    private UUID productId1;
    private UUID productId2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        productId1 = UUID.randomUUID();
        productId2 = UUID.randomUUID();
        cartId = UUID.randomUUID();
        user = new User("John Doe", "john.doe@example.com");
        cart = new Cart(user, new HashMap<>());
    }

    @Test
    void testFindAllCarts() {
        List<Cart> carts = Arrays.asList(cart, new Cart(user, new HashMap<>()));
        when(cartRepo.findAll()).thenReturn(carts);
        List<Cart> result = cartService.findAllCarts();
        assertEquals(2, result.size());
        assertTrue(result.contains(cart));
    }

    @Test
    void testFindCartById_CartExists() {
        when(cartRepo.findById(cartId)).thenReturn(Optional.of(cart));
        Cart result = cartService.findCartById(cartId);
        assertEquals(cart, result);
    }

    @Test
    void testFindCartById_CartDoesNotExist() {
        UUID nonExistentCartId = UUID.randomUUID();
        when(cartRepo.findById(nonExistentCartId)).thenReturn(Optional.empty());

        RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class, () -> {
            cartService.findCartById(nonExistentCartId);
        });
        assertEquals(CartService.class + " | record not found | id=" + nonExistentCartId, thrown.getMessage());
    }

    @Test
    void testFindCartByUserId_CartExists() {
        when(cartRepo.findByUser_Id(userId)).thenReturn(Optional.of(cart));
        Cart result = cartService.findCartByUserId(userId);
        assertEquals(cart, result);
        verify(cartRepo, never()).save(any(Cart.class));
    }

    @Test
    void testFindCartByUserId_CartDoesNotExist() {
        User newUser = new User("New User", "new@user.com");
        UUID newUserId = newUser.getId();
    
        when(userService.findUserById(newUserId)).thenReturn(newUser);
        when(cartRepo.findByUser_Id(newUserId)).thenReturn(Optional.empty());
        
        assertThrows(RecordNotFoundException.class, ()-> {
            cartService.findCartByUserId(newUserId);
        });
    
        verify(userService).findUserById(newUserId);
        verify(cartRepo).findByUser_Id(newUserId);
    }

    @Test
    void testValidateAndUpdateCartItems_AddNewItems() {
        cart.getCartItems().clear();
        
        when(cartRepo.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepo.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));
    
        Map<UUID, Integer> items = new HashMap<>();
        items.put(productId1, 2);
        items.put(productId2, 3);
    
        Cart result = cartService.validateAndUpdateCartItems(cartId, items);
    
        assertNotNull(result, "Cart should not be null");
        assertEquals(2, result.getCartItems().size(), "Cart should have 2 items");
        assertEquals(2, result.getCartItems().get(productId1), "Cart should have the correct quantity for productId1");
        assertEquals(3, result.getCartItems().get(productId2), "Cart should have the correct quantity for productId2");
        verify(cartRepo).findById(cartId);
        verify(cartRepo).save(result);
    }

    @Test
    void testValidateAndUpdateCartItems_UpdateExistingItem() {
        cart.getCartItems().put(productId1, 1);

        Map<UUID, Integer> items = new HashMap<>();
        items.put(productId1, 2);

        when(cartRepo.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepo.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Cart result = cartService.validateAndUpdateCartItems(cartId, items);

        assertEquals(1, result.getCartItems().size());
        assertEquals(2, result.getCartItems().get(productId1));
        verify(cartRepo).save(result);
    }

    @Test
    void testValidateAndUpdateCartItems_HandleInvalidProducts() {
        UUID invalidProductId = UUID.randomUUID();
        when(productService.findProductById(invalidProductId)).thenThrow(new RecordNotFoundException("Product not found"));
        Map<UUID, Integer> items = new HashMap<>();
        items.put(productId1, 2);
        items.put(invalidProductId, 1);
    
        when(cartRepo.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepo.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));
    
        BulkOperationException thrown = assertThrows(BulkOperationException.class, () -> {
            cartService.validateAndUpdateCartItems(cartId, items);
        });
    
        assertEquals("Bulk operation failed with errors: Product not found", thrown.getMessage());
    
        Cart result = cartService.findCartById(cartId);
        assertNotNull(result, "Cart should not be null");
        assertEquals(0, result.getCartItems().size(), "Cart should be empty (updates with invalid items will be rolled back)");
    
        verify(cartRepo, times(2)).findById(cartId);
        verify(productService).findProductById(productId1);
        verify(productService).findProductById(invalidProductId);
    }

    @Test
    public void testEmptyCart_RemovesAllItems() {
        cart.getCartItems().put(productId1, 2);
        when(cartRepo.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepo.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart result = cartService.emptyCart(cartId);

        assertNotNull(result, "Cart should not be null");
        assertTrue(result.getCartItems().isEmpty(), "Cart should be empty");
        verify(cartRepo).findById(cartId);
        verify(cartRepo).save(cart);
    }
}
