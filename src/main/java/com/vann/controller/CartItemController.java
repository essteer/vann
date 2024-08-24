package com.vann.controller;

import com.vann.model.CartItem;
import com.vann.service.CartItemService;
import com.vann.exceptions.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getAllCartItems() {
        List<CartItem> cartItems = cartItemService.findAllCartItems();
        if (cartItems.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/{cartItemId}")
    public ResponseEntity<CartItem> getCartItemById(@PathVariable UUID cartItemId) {
        try {
            CartItem cartItem = cartItemService.findCartItemById(cartItemId);
            return new ResponseEntity<>(cartItem, HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createCartItem(
            @RequestParam UUID productId,
            @RequestParam Integer quantity) {

        try {
            CartItem cartItem = cartItemService.createCartItem(productId, quantity);
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cartItem.getCartItemId())
                .toUri();
    
            return ResponseEntity.created(location).body(cartItem);
        } catch (RecordNotFoundException e) {
            Exception elaboratedException = new RecordNotFoundException("Product with ID '" + productId + "' not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(elaboratedException.getMessage());
        }
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(
            @PathVariable UUID cartItemId,
            @RequestBody CartItem updatedCartItem) {

        try {
            CartItem cartItem = cartItemService.updateCartItem(cartItemId, updatedCartItem);
            return new ResponseEntity<>(cartItem, HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable UUID cartItemId) {
        try {
            cartItemService.deleteCartItem(cartItemId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
