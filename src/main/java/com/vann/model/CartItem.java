package com.vann.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

@Entity
public class CartItem {

    @Id
    private UUID id;

    @JoinColumn(name = "product_id")
    private UUID productId;

    private Integer quantity;

    public CartItem() {
    }

    public CartItem(UUID productId, Integer quantity) {
        generateIdIfAbsent();
        this.productId = productId;
        this.quantity = quantity;
    }

    public void generateIdIfAbsent() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getCartItemId() {
        generateIdIfAbsent();
        return id;
    }

    public void setCartItemId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = Math.max(0, quantity);
    }

    @Override
    public String toString() {
        return "CartItem [id=" + id + ", productId=" + productId
                + ", quantity=" + quantity + "]";
    }

}
