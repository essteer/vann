package com.vann.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Invoice {

    @Id
    private UUID invoiceUuid;

    @ManyToOne
    @JoinColumn(name = "FK_customerUuid", referencedColumnName = "customerUuid")
    private Customer customer;

    private String billingAddress;
    private String shippingAddress;
    private double totalAmount;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceItem> invoiceItems;

    public Invoice() {
        this.invoiceUuid = UUID.randomUUID();
    }

    public Invoice(Customer customer, String billingAddress, String shippingAddress, List<InvoiceItem> invoiceItems, double totalAmount) {
        this();
        this.customer = customer;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
        this.invoiceItems = invoiceItems;
        this.totalAmount = totalAmount;
    }

    public UUID getInvoiceUuid() {
        return invoiceUuid;
    }

    public void setInvoiceUuid(UUID invoiceUuid) {
        this.invoiceUuid = invoiceUuid;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double calculateTotalAmount() {
        return invoiceItems.stream()
        .mapToDouble(ip -> ip.getPrice() * ip.getQuantity())
        .sum();
    }

    public List<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
        this.totalAmount = calculateTotalAmount();
    }

    @Override
    public String toString() {
        return "Invoice [invoiceUuid=" + invoiceUuid + ", customer=" + customer + ", billingAddress=" + billingAddress
                + ", shippingAddress=" + shippingAddress + ", totalAmount=" + totalAmount + ", invoiceItems="
                + invoiceItems + "]";
    }

}
