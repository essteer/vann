package com.vann.models;

import java.util.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "invoice_items")
@Data
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @JoinColumn(name = "fk_product_id", referencedColumnName = "id", nullable = false)
    private UUID productId;

    private int quantity;
    private double unitPrice;
    @Column(columnDefinition = "TEXT")
    private String productDetails;

    public InvoiceItem() {
    }

    public InvoiceItem(UUID productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void setProductId(UUID productId) {
        if (productId != null) {
            this.productId = productId;
        }
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        }
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
