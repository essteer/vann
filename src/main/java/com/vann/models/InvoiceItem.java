package com.vann.models;

import java.util.*;

import jakarta.persistence.*;


@Entity
@Table(name = "invoice_items")
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "fk_product_id", referencedColumnName = "id", nullable = false)
    private UUID productId;

    private int quantity;
    private double unitPrice;
    private String productDetails;

    public InvoiceItem() {
    }

    public InvoiceItem(UUID productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
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
        this.unitPrice = unitPrice;
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
