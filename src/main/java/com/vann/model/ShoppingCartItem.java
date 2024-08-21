package com.vann.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ShoppingCartItem {

    @Id
    private UUID shoppingCartItemUuid;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private ShoppingCart shoppingCart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    public ShoppingCartItem() {
        this.shoppingCartItemUuid = UUID.randomUUID();
    }

    public ShoppingCartItem(ShoppingCart cart, Product product, Integer quantity) {
        this();
        this.shoppingCart = cart;
        this.product = product;
        this.quantity = quantity;

    }

    public UUID getShoppingCartItemUuid() {
        return shoppingCartItemUuid;
    }

    public void setShoppingCartItemUuid(UUID shoppingCartItemUuid) {
        this.shoppingCartItemUuid = shoppingCartItemUuid;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
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
        return "ShoppingCartItem [shoppingCartItemUuid=" + shoppingCartItemUuid + ", shoppingCart=" + shoppingCart + ", product=" + product
                + ", quantity=" + quantity + "]";
    }

}
