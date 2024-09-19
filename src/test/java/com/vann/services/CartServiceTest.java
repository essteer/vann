package com.vann.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.models.*;
import com.vann.repositories.*;


public class CartServiceTest {

    @Mock
    private CartRepo cartRepo;

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private CustomerService customerService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    private Customer customer;
    private Product product1;
    private Product product2;
    private Cart cart;
    private UUID customerId;
    private UUID productId1;
    private UUID productId2;
    private UUID cartId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerId = UUID.randomUUID();
        productId1 = UUID.randomUUID();
        productId2 = UUID.randomUUID();
        cartId = UUID.randomUUID();
        customer = new Customer("John Doe", "john.doe@example.com");
        product1 = new Product(new Category().getId(), "Product 1", 10.0, "image1.png", null, null);
        product2 = new Product(new Category().getId(), "Product 2", 20.0, "image2.png", null, null);
        cart = new Cart(customerId, new HashMap<>());

        when(cartRepo.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepo.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
        when(productService.findProductById(productId1)).thenReturn(product1);
        when(productService.findProductById(productId2)).thenReturn(product2);
        when(customerService.findCustomerById(customerId)).thenReturn(customer);
    }

    @Test
    void testFindAllCarts() {
        List<Cart> carts = Arrays.asList(cart, new Cart(UUID.randomUUID(), new HashMap<>()));
        when(cartRepo.findAll()).thenReturn(carts);
        List<Cart> result = cartService.findAllCarts();
        assertEquals(2, result.size());
        assertTrue(result.contains(cart));
    }

    @Test
    void testFindCartById_CartExists() {
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

        assertEquals("Cart with ID '" + nonExistentCartId + "' not found", thrown.getMessage());
    }

    @Test
    void testCreateOrFindCartByCustomerId_CartExists() {
        Cart result = cartService.createOrFindCartByCustomerId(customerId);
        assertEquals(cart, result);
        verify(cartRepo, never()).save(any(Cart.class));
    }

    @Test
    void testCreateOrFindCartByCustomerId_CartDoesNotExist() {
        Customer newCustomer = new Customer("New Customer", "new@customer.com");
        UUID newCustomerId = newCustomer.getId();
    
        when(customerService.findCustomerById(newCustomerId)).thenReturn(newCustomer);
        when(cartRepo.findByCustomerId(newCustomerId)).thenReturn(Optional.empty());
        when(cartRepo.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));  // Return the cart being saved
        Cart result = cartService.createOrFindCartByCustomerId(newCustomerId);
    
        assertNotNull(result, "Cart should not be null");
        assertEquals(newCustomerId, result.getCartCustomerId(), "Cart should have the correct customer ID");
        assertTrue(result.getCartItems().isEmpty(), "New cart should be empty");
        verify(customerService).findCustomerById(newCustomerId);
        verify(cartRepo).findByCustomerId(newCustomerId);
        verify(cartRepo).save(result);
    }

    @Test
    void testAddOrUpdateCartItems_AddNewItems() {
        cart.getCartItems().clear();
        
        when(cartRepo.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepo.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));  // Return the cart being saved
    
        Map<UUID, Integer> items = new HashMap<>();
        items.put(productId1, 2);
        items.put(productId2, 3);
    
        Cart result = cartService.addOrUpdateCartItems(cartId, items);
    
        assertNotNull(result, "Cart should not be null");
        assertEquals(2, result.getCartItems().size(), "Cart should have 2 items");
        assertEquals(2, result.getCartItems().get(productId1), "Cart should have the correct quantity for productId1");
        assertEquals(3, result.getCartItems().get(productId2), "Cart should have the correct quantity for productId2");
        verify(cartRepo).findById(cartId);
        verify(cartRepo).save(result);
    }

    @Test
    void testAddOrUpdateCartItems_UpdateExistingItem() {
        cart.getCartItems().put(productId1, 1);

        Map<UUID, Integer> items = new HashMap<>();
        items.put(productId1, 2);

        Cart result = cartService.addOrUpdateCartItems(cartId, items);

        assertEquals(1, result.getCartItems().size());
        assertEquals(3, result.getCartItems().get(productId1));
        verify(cartRepo).save(result);
    }

    @Test
    void testAddOrUpdateCartItems_HandleInvalidProducts() {
        UUID invalidProductId = UUID.randomUUID();
        when(productService.findProductById(invalidProductId)).thenThrow(new RecordNotFoundException("Product not found"));
        Map<UUID, Integer> items = new HashMap<>();
        items.put(productId1, 2);
        items.put(invalidProductId, 1);
    
        when(cartRepo.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepo.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));  // Return the cart being saved
    
        RecordNotFoundException thrown = assertThrows(RecordNotFoundException.class, () -> {
            cartService.addOrUpdateCartItems(cartId, items);
        });
    
        assertEquals("Products with invalid IDs not updated: [" + invalidProductId.toString() + "]", thrown.getMessage());
    
        Cart result = cartService.findCartById(cartId);
        assertNotNull(result, "Cart should not be null");
        assertEquals(1, result.getCartItems().size(), "Cart should contain only valid items");
        assertEquals(2, result.getCartItems().get(productId1), "Cart should have the correct quantity for the valid product");
    
        verify(cartRepo, times(2)).findById(cartId);
        verify(cartRepo).save(result);
        verify(productService).findProductById(productId1);
        verify(productService).findProductById(invalidProductId);
    }

    @Test
    public void testEmptyCart_RemovesAllItems() {
        cart.getCartItems().put(productId1, 2);
        when(cartRepo.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepo.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));  // Return the cart being saved

        Cart result = cartService.emptyCart(cartId);

        assertNotNull(result, "Cart should not be null");
        assertTrue(result.getCartItems().isEmpty(), "Cart should be empty");
        verify(cartRepo).findById(cartId);
        verify(cartRepo).save(cart);
    }
}
