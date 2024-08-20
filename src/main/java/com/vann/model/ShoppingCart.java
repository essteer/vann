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
public class ShoppingCart {

    @Id
    private UUID cartUuid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_customerUuid", referencedColumnName = "customerUuid")
    private Customer customer;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ShoppingCartItem> items = new HashSet<>();

    public ShoppingCart() {
        this.cartUuid = UUID.randomUUID();
    }

    public ShoppingCart(Customer customer, Set<ShoppingCartItem> items) {
        this();
        this.customer = customer;
        this.items = items;
    }

    public UUID getCartUuid() {
        return cartUuid;
    }

    public void setCartUuid(UUID cartUuid) {
        this.cartUuid = cartUuid;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<ShoppingCartItem> getItems() {
        return items;
    }

    public void setItems(Set<ShoppingCartItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ShoppingCart [cartUuid=" + cartUuid + ", customer=" + customer + ", items=" + items + "]";
    }

}
