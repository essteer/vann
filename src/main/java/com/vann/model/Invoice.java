package com.vann.model;

import java.util.ArrayList;
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
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "FK_customer_id", referencedColumnName = "id")
    private Customer invoiceCustomer;

    private String invoiceBillAddress;
    private String invoiceShipAddress;
    private double invoiceTotalAmount;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceItem> invoiceItems;

    public Invoice() {
    }

    public Invoice(Customer customer, String billAddress, String shipAddress, List<InvoiceItem> invoiceItems, double totalAmount) {
        generateId();
        this.invoiceCustomer = customer;
        this.invoiceBillAddress = billAddress;
        this.invoiceShipAddress = shipAddress;
        this.invoiceItems = invoiceItems;
        this.invoiceTotalAmount = totalAmount;
    }

    public void generateId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getInvoiceId() {
        return id;
    }

    public void setInvoiceId(UUID id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return invoiceCustomer;
    }

    public void setCustomer(Customer customer) {
        this.invoiceCustomer = customer;
    }

    public String getBillAddress() {
        return invoiceBillAddress;
    }

    public void setBillAddress(String billAddress) {
        this.invoiceBillAddress = billAddress;
    }

    public String getShipAddress() {
        return invoiceShipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.invoiceShipAddress = shipAddress;
    }

    public double getTotalAmount() {
        return invoiceTotalAmount;
    }

    public double calculateTotalAmount() {
        return invoiceItems.stream()
        .mapToDouble(ip -> ip.getPrice() * ip.getQuantity())
        .sum();
    }

    public List<InvoiceItem> getInvoiceItems() {
        if (invoiceItems == null) {
            this.invoiceItems = new ArrayList<>();
        }
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
        this.invoiceTotalAmount = calculateTotalAmount();
    }

    @Override
    public String toString() {
        return "Invoice [id=" + id + ", customer=" + invoiceCustomer + ", billAddress=" + invoiceBillAddress
                + ", shipAddress=" + invoiceShipAddress + ", totalAmount=" + invoiceTotalAmount + "]";
    }

}
