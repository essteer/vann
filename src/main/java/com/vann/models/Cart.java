package com.vann.models;

import java.util.*;

import com.vann.utils.LogHandler;

import jakarta.persistence.*;

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
        try {
            generateIdIfAbsent();
            setCartCustomerId(customerId);
            this.cartItems = cartItems;
            LogHandler.createInstanceOK(Cart.class, this.id, String.valueOf(this.customerId), this.cartItems.toString());
        } catch (Exception e) {
            LogHandler.createInstanceError(Cart.class, customerId, cartItems.toString());
            throw e;
        }
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
        if (id == null) {
            LogHandler.nullAttributeWarning(Cart.class, getCartId(), "id");
        } else {
            this.id = id;
            LogHandler.validAttributeOK(Cart.class, getCartId(), "id", String.valueOf(getCartId()));
        }
    }

    public UUID getCartCustomerId() {
        return customerId;
    }

    public void setCartCustomerId(UUID customerId) {
        if (customerId == null) {
            LogHandler.nullAttributeWarning(Cart.class, getCartId(), "customerId");
        } else {
            this.customerId = customerId;
            LogHandler.validAttributeOK(Cart.class, getCartId(), "customerId", String.valueOf(customerId));
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
