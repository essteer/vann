package com.vann.models;

import java.util.UUID;

import com.vann.utils.LogHandler;

import jakarta.persistence.*;

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
        try {
            generateIdIfAbsent();
            setInvoiceItemProductId(productId);
            setQuantity(quantity);
            LogHandler.createInstanceOK(InvoiceItem.class, this.id, String.valueOf(this.productId), String.valueOf(this.quantity));
        } catch (Exception e) {
            LogHandler.createInstanceError(InvoiceItem.class, productId, quantity);
            throw e;
        }
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
        if (id == null) {
            LogHandler.nullAttributeWarning(InvoiceItem.class, getInvoiceItemId(), "id");
        } else {
            this.id = id;
            LogHandler.validAttributeOK(InvoiceItem.class, getInvoiceItemId(), "id", String.valueOf(getInvoiceItemId()));
        }
    }

    public UUID getInvoiceItemProductId() {
        return productId;
    }

    public void setInvoiceItemProductId(UUID productId) {
        if (productId == null) {
            LogHandler.nullAttributeWarning(InvoiceItem.class, getInvoiceItemId(), "invoiceItemProductId");
        } else {
            this.productId = productId;
            LogHandler.validAttributeOK(InvoiceItem.class, getInvoiceItemId(), "invoiceItemProductId", String.valueOf(getInvoiceItemProductId()));
        }
    }
    
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws IllegalArgumentException {
        if (quantity >= 0) {
            this.quantity = quantity;
            LogHandler.validAttributeOK(InvoiceItem.class, getInvoiceItemId(), "quantity", String.valueOf(getQuantity()));
        } else {
            LogHandler.invalidAttributeError(InvoiceItem.class, getInvoiceItemId(), "quantity", String.valueOf(getQuantity()), "Quantity cannot be negative");
            throw new IllegalArgumentException("Quantity cannot be negative: " + quantity);
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
