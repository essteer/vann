package com.vann.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.model.Cart;
import com.vann.service.CartService;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> carts = cartService.findAllCarts();
        if (carts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(carts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCart(@PathVariable UUID id) {
        try {
            Cart cart = cartService.findCartById(id);
            return ResponseEntity.ok(cart);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/customer-id/{id}")
    public ResponseEntity<?> getCartByCustomerId(@PathVariable UUID id) {
        try {
            Cart cart = cartService.findOrCreateCartByCustomerId(id);
            return ResponseEntity.ok(cart);
        } catch (RecordNotFoundException e) {  // if customer not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/customer-email/{email}")
    public ResponseEntity<?> getCartByCustomerEmail(@PathVariable String email) {
        try {
            Cart cart = cartService.findOrCreateCartByCustomerEmail(email);
            return ResponseEntity.ok(cart);
        } catch (RecordNotFoundException e) {  // if customer not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/update-items/{cartId}")
    public ResponseEntity<?> updateCartItems(
            @PathVariable UUID cartId,
            @RequestBody Map<UUID, Integer> items) {
        try {
            Cart updatedCart = cartService.addOrUpdateCartItems(cartId, items);
            return ResponseEntity.ok(updatedCart);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/empty/{id}")
    public ResponseEntity<?> emptyCart(@PathVariable UUID id) {
        try {
            Cart cart = cartService.emptyCart(id);
            return ResponseEntity.ok(cart);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
