package com.vann.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Cart {

    @Id
    private UUID id;

    @JoinColumn(name = "FK_customer_id", referencedColumnName = "id", nullable = false)
    private UUID customerId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private Set<CartItem> cartItems = new HashSet<>();

    public Cart() {
    }

    public Cart(UUID customerId, Set<CartItem> cartItems) {
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

    public Set<CartItem> getCartItems() {
        if (cartItems == null) {
            this.cartItems = new HashSet<>();
        }
        return cartItems;
    }

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public String toString() {
        return "Cart [id=" + id + ", customerId=" + customerId + "]";
    }

}
