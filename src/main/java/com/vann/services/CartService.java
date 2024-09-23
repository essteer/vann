package com.vann.services;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vann.exceptions.*;
import com.vann.models.*;
import com.vann.repositories.CartRepo;
import com.vann.utils.LogHandler;


@Service
public class CartService {

    private final CartRepo cartRepo;
    private final CustomerService customerService;
    private final InvoiceService invoiceService;
    private final ProductService productService;

    public CartService(CartRepo cartRepo, CustomerService customerService, InvoiceService invoiceService, ProductService productService) {
        this.cartRepo = cartRepo;
        this.customerService = customerService;
        this.invoiceService = invoiceService;
        this.productService = productService;
    }

    @Transactional
    public Cart createOrFindCartByCustomerId(UUID customerId) throws RecordNotFoundException {
        Customer customer = customerService.findCustomerById(customerId);
        try {
            Cart cart = findCartByCustomerId(customerId);
            LogHandler.status200OK(CartService.class + " | record found | id=" + cart.getId());
            return cart;
        } catch (RecordNotFoundException e) {
            Cart newCart = saveCart(new Cart(customer, new HashMap<>()));
            LogHandler.status201Created(CartService.class + " | 1 record created | id=" + newCart.getId());
            return newCart;
        }
    }

    private Cart findCartByCustomerId(UUID customerId) throws RecordNotFoundException {
        Optional<Cart> cartOptional = cartRepo.findByCustomer_Id(customerId);
        
        if (cartOptional.isPresent()) {
            LogHandler.status200OK(CartService.class + " | record found | customerId=" + customerId);
            return cartOptional.get();
        } else {
            throw new RecordNotFoundException(CartService.class + " | record not found | id=" + customerId);
        }
    }

    @Transactional
    private Cart saveCart(Cart cart) {
        return cartRepo.save(cart);
    }

    public List<Cart> findAllCarts() {
        List<Cart> carts = cartRepo.findAll();
        logBulkFindOperation(carts, "findAll()");
        return carts;
    }

    private void logBulkFindOperation(List<Cart> carts, String details) {
        if (carts.isEmpty()) {
            LogHandler.status204NoContent(CartService.class + " | 0 records | " + details);    
        } else {
            LogHandler.status200OK(CartService.class + " | " + carts.size() + " records | " + details);
        }
    }

    public Cart findCartById(UUID id) throws RecordNotFoundException {
        Optional<Cart> cartOptional = cartRepo.findById(id);
        
        if (cartOptional.isPresent()) {
            LogHandler.status200OK(CartService.class + " | record found | id=" + id);
            return cartOptional.get();
        } else {
            throw new RecordNotFoundException(CartService.class + " | record not found | id=" + id);
        }
    }

    @Transactional
    public Cart addOrUpdateCartItems(UUID cartId, Map<UUID, Integer> items) throws RecordNotFoundException {
        Cart cart = findCartById(cartId);
        List<String> errorMessages = new ArrayList<>();
    
        for (Map.Entry<UUID, Integer> entry : items.entrySet()) {
            UUID productId = entry.getKey();
            int quantity = entry.getValue();
            try {
                validateProductId(productId);
                addOrUpdateCartItem(cart, productId, quantity);
            } catch (RecordNotFoundException e) {
                errorMessages.add(e.getMessage());
            } catch (Exception e) {
                errorMessages.add(e.getMessage());
            }
        }

        if (errorMessages.isEmpty()) {
            Cart savedCart = saveCart(cart);
            LogHandler.status200OK(CartService.class + " | " + items.size() + " entries updated");
            return savedCart;
        } else {
            throw new BulkOperationException(errorMessages);
        }
    }

    private void validateProductId(UUID productId) throws RecordNotFoundException {
        productService.findProductById(productId);
    }

    @Transactional
    private void addOrUpdateCartItem(Cart cart, UUID productId, int quantity) {
        int existingQuantity = findExistingCartItem(cart, productId);
        if (existingQuantity >= 0) {
            updateExistingCartItem(cart, productId, quantity);
        } else {
            addNewCartItem(cart, productId, quantity);
        }
    }

    private int findExistingCartItem(Cart cart, UUID productId) {
        return cart.getCartItems().getOrDefault(productId, 0);
    }

    @Transactional
    private void updateExistingCartItem(Cart cart, UUID productId, int quantity) {
        int updatedQuantity = cart.getCartItems().getOrDefault(productId, 0) + quantity;
        if (updatedQuantity <= 0) {
            cart.getCartItems().remove(productId);
        } else {
            cart.getCartItems().put(productId, updatedQuantity);
        }
    }

    @Transactional
    private void addNewCartItem(Cart cart, UUID productId, int quantity) {
        cart.getCartItems().put(productId, quantity);
    }

    @Transactional
    public Invoice checkoutCart(UUID cartId, String billAndShipAddress) throws RecordNotFoundException {
        return checkoutCart(cartId, billAndShipAddress, billAndShipAddress);
    }

    @Transactional
    public Invoice checkoutCart(UUID id, String billAddress, String shipAddress) throws RecordNotFoundException, IllegalArgumentException {
        Cart cart = findCartById(id);  // throws RecordNotFoundException
        if (isCartEmpty(cart)) {
            throw new IllegalArgumentException(CartService.class + " | empty cart cannot be checked out | id=" + id);
        }
        Invoice invoice = invoiceService.createInvoice(cart.getCustomer(), cart.getCartItems(), billAddress, shipAddress);
        emptyCart(id);

        return invoice;
    }

    private boolean isCartEmpty(Cart cart) {
        return cart.getCartItems().isEmpty();
    }

    @Transactional
    public Cart emptyCart(UUID id) {
        Cart cart = findCartById(id);
        cart.getCartItems().clear();
        LogHandler.status204NoContent(CartService.class + " | cart emptied | id=" + id);
        return saveCart(cart);
    }

}
