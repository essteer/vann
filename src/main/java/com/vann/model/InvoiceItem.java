package com.vann.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

@Entity
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
        this.productId = productId;
        this.quantity = quantity;
    }

    public void generateIdIfAbsent() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getInvoiceItemId() {
        generateIdIfAbsent();
        return id;
    }

    public void setInvoiceItemId(UUID id) {
        this.id = id;
    }

    public UUID getInvoiceItemProductId() {
        return productId;
    }

    public void setInvoiceItemProductId(UUID productId) {
        this.productId = productId;
    }
    
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
