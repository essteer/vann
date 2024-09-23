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
    private CustomerRepo customerRepo;

    @Mock
    private CustomerService customerService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    private Cart cart;
    private Customer customer;
    private UUID cartId;
    private UUID customerId;
    private UUID productId1;
    private UUID productId2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerId = UUID.randomUUID();
        productId1 = UUID.randomUUID();
        productId2 = UUID.randomUUID();
        cartId = UUID.randomUUID();
        customer = new Customer("John Doe", "john.doe@example.com");
        cart = new Cart(customer, new HashMap<>());
    }

    @Test
    void testFindAllCarts() {
        List<Cart> carts = Arrays.asList(cart, new Cart(customer, new HashMap<>()));
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
    void testCreateOrFindCartByCustomerId_CartExists() {
        when(cartRepo.findByCustomer_Id(customerId)).thenReturn(Optional.of(cart));
        Cart result = cartService.createOrFindCartByCustomerId(customerId);
        assertEquals(cart, result);
        verify(cartRepo, never()).save(any(Cart.class));
    }

    @Test
    void testCreateOrFindCartByCustomerId_CartDoesNotExist() {
        Customer newCustomer = new Customer("New Customer", "new@customer.com");
        UUID newCustomerId = newCustomer.getId();
    
        when(customerService.findCustomerById(newCustomerId)).thenReturn(newCustomer);
        when(cartRepo.findByCustomer_Id(newCustomerId)).thenReturn(Optional.empty());
        when(cartRepo.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Cart result = cartService.createOrFindCartByCustomerId(newCustomerId);
    
        assertNotNull(result, "Cart should not be null");
        assertEquals(newCustomer, result.getCustomer(), "Cart should have the correct customer");
        assertTrue(result.getCartItems().isEmpty(), "New cart should be empty");
        verify(customerService).findCustomerById(newCustomerId);
        verify(cartRepo).findByCustomer_Id(newCustomerId);
        verify(cartRepo).save(result);
    }

    @Test
    void testAddOrUpdateCartItems_AddNewItems() {
        cart.getCartItems().clear();
        
        when(cartRepo.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepo.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));
    
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

        when(cartRepo.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepo.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));
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
        when(cartRepo.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));
    
        BulkOperationException thrown = assertThrows(BulkOperationException.class, () -> {
            cartService.addOrUpdateCartItems(cartId, items);
        });
    
        assertEquals("Bulk operation failed with errors: Product not found", thrown.getMessage());
    
        Cart result = cartService.findCartById(cartId);
        assertNotNull(result, "Cart should not be null");
        assertEquals(1, result.getCartItems().size(), "Cart should contain only valid items");
        assertEquals(2, result.getCartItems().get(productId1), "Cart should have the correct quantity for the valid product");
    
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
