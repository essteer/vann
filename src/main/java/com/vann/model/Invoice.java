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
    private UUID invoiceId;

    @ManyToOne
    @JoinColumn(name = "FK_customerId", referencedColumnName = "customerId")
    private Customer customer;

    private String billAddress;
    private String shipAddress;
    private double totalAmount;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceItem> invoiceItems;

    public Invoice() {
        this.invoiceId = UUID.randomUUID();
    }

    public Invoice(Customer customer, String billAddress, String shipAddress, List<InvoiceItem> invoiceItems, double totalAmount) {
        this();
        this.customer = customer;
        this.billAddress = billAddress;
        this.shipAddress = shipAddress;
        this.invoiceItems = invoiceItems;
        this.totalAmount = totalAmount;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getBillAddress() {
        return billAddress;
    }

    public void setBillAddress(String billAddress) {
        this.billAddress = billAddress;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
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
        return "Invoice [invoiceId=" + invoiceId + ", customer=" + customer + ", billAddress=" + billAddress
                + ", shipAddress=" + shipAddress + ", totalAmount=" + totalAmount + ", invoiceItems="
                + invoiceItems + "]";
    }

}
