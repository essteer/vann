package com.vann.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class InvoiceItem {

    @Id
    private UUID invoiceItemUuid;

    @ManyToOne
    @JoinColumn(name = "FK_invoiceUuid")
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "FK_itemUuid")
    private Product invoiceItem;

    private int quantity;
    private double price;

    public InvoiceItem() {
        this.invoiceItemUuid = UUID.randomUUID();
    }

    public InvoiceItem(Invoice invoice, Product product, int quantity, double price) {
        this();
        this.invoice = invoice;
        this.invoiceItem = product;
        this.quantity = quantity;
        this.price = price;
    }

    public UUID getInvoiceItemUuid() {
        return invoiceItemUuid;
    }

    public void setInvoiceItemUuid(UUID invoiceItemUuid) {
        this.invoiceItemUuid = invoiceItemUuid;
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
        return "InvoiceItem [invoiceItemUuid=" + invoiceItemUuid + ", invoice=" + invoice + ", invoiceItem="
                + invoiceItem + ", quantity=" + quantity + ", price=" + price + "]";
    }

}
