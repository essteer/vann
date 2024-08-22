package com.vann.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class CartItem {

    @Id
    private UUID cartItemUuid;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    public CartItem() {
        this.cartItemUuid = UUID.randomUUID();
    }

    public CartItem(Cart cart, Product product, Integer quantity) {
        this();
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;

    }

    public UUID getCartItemUuid() {
        return cartItemUuid;
    }

    public void setCartItemUuid(UUID cartItemUuid) {
        this.cartItemUuid = cartItemUuid;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem [cartItemUuid=" + cartItemUuid + ", cart=" + cart + ", product=" + product
                + ", quantity=" + quantity + "]";
    }

}