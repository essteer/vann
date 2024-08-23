package com.vann.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Cart {

    @Id
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_customer_id", referencedColumnName = "id")
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private Set<CartItem> cartItems = new HashSet<>();

    public Cart() {
    }

    public Cart(Customer customer, Set<CartItem> cartItems) {
        generateId();
        this.customer = customer;
        this.cartItems = cartItems;
    }

    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getCartId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        return id;
    }

    public void setCartId(UUID id) {
        this.id = id;
    }

    public Customer getCartCustomer() {
        return customer;
    }

    public void setCartCustomer(Customer customer) {
        this.customer = customer;
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
        return "Cart [id=" + id + ", customer=" + customer + "]";
    }

}
