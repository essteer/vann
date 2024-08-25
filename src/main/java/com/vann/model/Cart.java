package com.vann.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;

@Entity
public class Cart {

    @Id
    private UUID id;

    @JoinColumn(name = "FK_customer_id", referencedColumnName = "id", nullable = false)
    private UUID customerId;

    @ElementCollection
    @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Integer> cartItems = new HashMap<>();

    public Cart() {
    }

    public Cart(UUID customerId, Map<UUID, Integer> cartItems) {
        generateIdIfAbsent();
        this.customerId = customerId;
        this.cartItems = cartItems;
    }

    public void generateIdIfAbsent() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getCartId() {
        generateIdIfAbsent();
        return id;
    }

    public void setCartId(UUID id) {
        this.id = id;
    }

    public UUID getCartCustomerId() {
        return customerId;
    }

    public void setCartCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public Map<UUID, Integer> getCartItems() {
        if (cartItems == null) {
            this.cartItems = new HashMap<>();
        }
        return cartItems;
    }

    public void setCartItems(Map<UUID, Integer> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public String toString() {
        return "Cart [id=" + id + ", customerId=" + customerId + "]";
    }

}
