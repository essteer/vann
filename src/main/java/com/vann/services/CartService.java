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
    public Cart findCartByCustomerId(UUID id) {
        customerService.findCustomerById(id);
        return findCartByExistingCustomerId(id);
    }

    private Cart findCartByExistingCustomerId(UUID id) throws RecordNotFoundException {
        Optional<Cart> cartOptional = cartRepo.findByCustomer_Id(id);
        
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            LogHandler.status200OK(CartService.class + " | record found | id=" + cart.getId());
            return cart;
        } else {
            throw new RecordNotFoundException(CartService.class + " | record not found | customerId=" + id);
        }
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
    public Cart validateAndUpdateCartItems(UUID id, Map<UUID, Integer> items) throws BulkOperationException {
        Cart cart = findCartById(id);
        List<String> errorMessages = validateProductIds(items);
    
        if (errorMessages.isEmpty()) {
            return updateCartItems(cart, items);
        } else {
            throw new BulkOperationException(errorMessages);
        }
    }

    private List<String> validateProductIds(Map<UUID, Integer> items) {
        List<String> errorMessages = new ArrayList<>();
        for (Map.Entry<UUID, Integer> entry : items.entrySet()) {
            UUID id = entry.getKey();
            try {
                validateProductId(id);
            } catch (RecordNotFoundException e) {
                errorMessages.add(e.getMessage());
            } catch (Exception e) {
                errorMessages.add(e.getMessage());
            }
        }
        return errorMessages;
    }

    private void validateProductId(UUID id) throws RecordNotFoundException {
        productService.findProductById(id);
    }

    private Cart updateCartItems(Cart cart, Map<UUID, Integer> items) {
        cart.setCartItems(items);
        Cart savedCart = saveCart(cart);
        LogHandler.status200OK(CartService.class + " | " + items.size() + " entries updated");
        return savedCart;
    }

    @Transactional
    private Cart saveCart(Cart cart) {
        return cartRepo.save(cart);
    }

    @Transactional
    public Invoice checkoutCart(UUID id, String billAndShipAddress) throws RecordNotFoundException {
        return checkoutCart(id, billAndShipAddress, billAndShipAddress);
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
