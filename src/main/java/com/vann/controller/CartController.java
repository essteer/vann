package com.vann.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vann.model.Cart;
import com.vann.service.CartService;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> Carts = cartService.findAllCarts();
        return ResponseEntity.ok(Carts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable UUID id) {
        Cart cart = cartService.findCartById(id);
        return ResponseEntity.ok(cart);
    }

    @PostMapping
    public ResponseEntity<Cart> saveCart(@RequestBody Cart Cart) {
        Cart savedCart = cartService.saveCart(Cart);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedCart.getCartId())
            .toUri();
        return ResponseEntity.created(location).build();
        // return ResponseEntity.created(location).body(savedCart);
    }

}
