package com.vann.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class InvoiceProduct {

    @Id
    private UUID invoiceProductUuid;

    @ManyToOne
    @JoinColumn(name = "FK_invoiceUuid")
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "FK_productUuid")
    private Product product;

    private int quantity;
    private double price;

    public InvoiceProduct() {
        this.invoiceProductUuid = UUID.randomUUID();
    }

    public InvoiceProduct(Invoice invoice, Product product, int quantity, double price) {
        this();
        this.invoice = invoice;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public UUID getInvoiceProductUuid() {
        return invoiceProductUuid;
    }

    public void setInvoiceProductUuid(UUID invoiceProductUuid) {
        this.invoiceProductUuid = invoiceProductUuid;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
        return "InvoiceProduct [invoiceProductUuid=" + invoiceProductUuid + ", invoice=" + invoice + ", product="
                + product + ", quantity=" + quantity + ", price=" + price + "]";
    }

}
