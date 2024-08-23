package com.vann.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class InvoiceItem {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "FK_invoice_id")
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "FK_item_id")
    private Product invoiceItem;

    private int quantity;
    private double price;

    public InvoiceItem() {
    }

    public InvoiceItem(Invoice invoice, Product product, int quantity, double price) {
        generateId();
        this.invoice = invoice;
        this.invoiceItem = product;
        this.quantity = quantity;
        this.price = price;
    }

    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getInvoiceItemId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        return id;
    }

    public void setInvoiceItemId(UUID id) {
        this.id = id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Product getInvoiceItem() {
        return invoiceItem;
    }

    public void setInvoiceItem(Product invoiceItem) {
        this.invoiceItem = invoiceItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "InvoiceItem [id=" + id + ", invoice=" + invoice + ", invoiceItem="
                + invoiceItem + ", quantity=" + quantity + ", price=" + price + "]";
    }

}
