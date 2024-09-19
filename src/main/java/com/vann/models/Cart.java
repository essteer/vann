package com.vann.models;

import java.util.*;

import jakarta.persistence.*;


@Entity
@Table(name = "carts")
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
        setCartCustomerId(customerId);
        this.cartItems = cartItems;
    }

    public void generateIdIfAbsent() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getId() {
        generateIdIfAbsent();
        return id;
    }

    public void setId(UUID id) {
        if (id != null) {
            this.id = id;
        }
    }

    public UUID getCartCustomerId() {
        return customerId;
    }

    public void setCartCustomerId(UUID customerId) {
        if (customerId != null) {
            this.customerId = customerId;
        }
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
