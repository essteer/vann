package com.vann.models;

import java.util.*;

import jakarta.persistence.*;


@Entity
@Table(name = "invoice_items")
public class InvoiceItem {

    @Id
    private UUID id;

    @JoinColumn(name = "FK_product_id", referencedColumnName = "id", nullable = false)
    private UUID productId;

    private int quantity;
    private double unitPrice;
    private String productDetails;

    public InvoiceItem() {
    }

    public InvoiceItem(UUID productId, int quantity) {
        generateIdIfAbsent();
        setProductId(productId);
        setQuantity(quantity);
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

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        if (productId != null) {
            this.productId = productId;
        }
    }
    
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        }
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;  // negative unitPrice permitted (discounts, refunds, etc)
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public double calculateInvoiceItemSubtotal() {
        return unitPrice * quantity;
    }

    @Override
    public String toString() {
        return "InvoiceItem [id=" + id + ", unitprice=" + unitPrice + ", quantity=" + quantity + ", subtotal=" + calculateInvoiceItemSubtotal() + 
        ", Product=[" + productDetails + "]]";
    }

}
