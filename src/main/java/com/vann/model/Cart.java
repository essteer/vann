package com.vann.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Cart {

    @Id
    private UUID cartId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_customerId", referencedColumnName = "customerId")
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CartItem> cartItems = new HashSet<>();

    public Cart() {
        this.cartId = UUID.randomUUID();
    }

    public Cart(Customer customer, Set<CartItem> cartItems) {
        this();
        this.customer = customer;
        this.cartItems = cartItems;
    }

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public Customer getCartCustomer() {
        return customer;
    }

    public void setCartCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public String toString() {
        return "Cart [cartId=" + cartId + ", customer=" + customer + ", cartItems=" + cartItems + "]";
    }

}
