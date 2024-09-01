package com.vann.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.vann.exceptions.FieldConflictException;
import com.vann.exceptions.RecordNotFoundException;
import com.vann.model.Cart;
import com.vann.model.Invoice;
import com.vann.service.CartService;
import com.vann.utils.LogHandler;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        String methodName = "getAllCarts()";
        try {
            List<Cart> carts = cartService.findAllCarts();
            if (carts.isEmpty()) {
                LogHandler.status204NoContent("GET", CartController.class, methodName);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            LogHandler.status200OK("GET", CartController.class, methodName);
            return ResponseEntity.ok(carts);

        } catch (Exception e) {
            LogHandler.status500InternalServerError("GET", CartController.class, methodName, e.getMessage());
            throw e;
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCart(@PathVariable UUID id) {
        String methodName = "getCart()";
        try {
            Cart cart = cartService.findCartById(id);
            LogHandler.status200OK("GET", CartController.class, methodName);
            return ResponseEntity.ok(cart);

        } catch (RecordNotFoundException e) {
            LogHandler.status404NotFound("GET", CartController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("GET", CartController.class, methodName, e.getMessage());
            throw e;
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/customer-id/{id}")
    public ResponseEntity<?> getCartByCustomerId(@PathVariable UUID id) {
        String methodName = "getCartByCustomerId()";
        try {
            Cart cart = cartService.createOrFindCartByCustomerId(id);
            LogHandler.status200OK("GET", CartController.class, methodName);
            return ResponseEntity.ok(cart);

        } catch (RecordNotFoundException e) { // if customer not found
            LogHandler.status404NotFound("GET", CartController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("GET", CartController.class, methodName, e.getMessage());
            throw e;
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCartItems(
            @PathVariable UUID id,
            @RequestBody Map<UUID, Integer> items) {
        String methodName = "updateCartItems()";
        try {
            Cart updatedCart = cartService.addOrUpdateCartItems(id, items);
            LogHandler.status200OK("PUT", CartController.class, methodName);
            return ResponseEntity.ok(updatedCart);

        } catch (IllegalArgumentException | MethodArgumentTypeMismatchException e) {
            LogHandler.status400BadRequest("PUT", CartController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RecordNotFoundException e) {
            LogHandler.status404NotFound("PUT", CartController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("PUT", CartController.class, methodName, e.getMessage());
            throw e;
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/checkout/{id}")
    public ResponseEntity<?> checkoutCart(
            @PathVariable UUID id,
            @RequestParam String billAddress,
            @RequestParam(required = false) String shipAddress) {
        String methodName = "checkoutCart()";
        try {
            if (shipAddress == null) {
                Invoice invoice = cartService.checkoutCart(id, billAddress);
                LogHandler.status200OK("PUT", CartController.class, methodName);
                return ResponseEntity.ok(invoice);
            } else {
                Invoice invoice = cartService.checkoutCart(id, billAddress, shipAddress);
                LogHandler.status200OK("PUT", CartController.class, methodName);
                return ResponseEntity.ok(invoice);
            }
        } catch (IllegalArgumentException | MethodArgumentTypeMismatchException e) {
            LogHandler.status400BadRequest("PUT", CartController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RecordNotFoundException e) {
            LogHandler.status404NotFound("PUT", CartController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (FieldConflictException e) { // if invoice in conflict
            LogHandler.status409Conflict("PUT", CartController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("PUT", CartController.class, methodName, e.getMessage());
            throw e;
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> emptyCart(@PathVariable UUID id) {
        String methodName = "emptyCart()";
        try {
            Cart cart = cartService.emptyCart(id);
            LogHandler.status204NoContent("DELETE", CartController.class, methodName);
            return ResponseEntity.ok(cart);

        } catch (RecordNotFoundException e) {
            LogHandler.status404NotFound("DELETE", CartController.class, methodName, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("DELETE", CartController.class, methodName, e.getMessage());
            throw e;
        }
    }

}
