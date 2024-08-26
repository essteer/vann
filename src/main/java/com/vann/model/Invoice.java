package com.vann.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Invoice {

    @Id
    private UUID id;

    private UUID invoiceCustomerId;
    private String invoiceCustomerName;
    private String invoiceCustomerEmail;

    private String invoiceBillAddress;
    private String invoiceShipAddress;

    private double invoiceTotalAmount;

    @OneToMany(cascade = CascadeType.ALL)
    private List<InvoiceItem> invoiceItems = new ArrayList<>();

    public Invoice() {
    }

    public Invoice(UUID customerId, String customerName, String customerEmail, String billAddress, String shipAddress) {
        generateIdIfAbsent();
        this.invoiceCustomerId = customerId;
        this.invoiceCustomerName = customerName;
        this.invoiceCustomerEmail = customerEmail;
        this.invoiceBillAddress = billAddress;
        this.invoiceShipAddress = shipAddress;
    }

    public Invoice(UUID customerId, String customerName, String customerEmail, String billAddress) {
        this(customerId, customerName, customerEmail, billAddress, billAddress);
    }

    public void generateIdIfAbsent() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getInvoiceId() {
        generateIdIfAbsent();
        return id;
    }

    public void setInvoiceId(UUID id) {
        this.id = id;
    }

    public UUID getInvoiceCustomerId() {
        return invoiceCustomerId;
    }

    public void setInvoiceCustomerId(UUID invoiceCustomerId) {
        this.invoiceCustomerId = invoiceCustomerId;
    }

    public String getInvoiceCustomerName() {
        return invoiceCustomerName;
    }

    public void setInvoiceCustomerName(String invoiceCustomerName) {
        this.invoiceCustomerName = invoiceCustomerName;
    }

    public String getInvoiceCustomerEmail() {
        return invoiceCustomerEmail;
    }

    public void setInvoiceCustomerEmail(String invoiceCustomerEmail) {
        this.invoiceCustomerEmail = invoiceCustomerEmail;
    }

    public String getInvoiceBillAddress() {
        return invoiceBillAddress;
    }

    public void setInvoiceBillAddress(String invoiceBillAddress) {
        this.invoiceBillAddress = invoiceBillAddress;
    }

    public String getInvoiceShipAddress() {
        return invoiceShipAddress;
    }

    public void setInvoiceShipAddress(String invoiceShipAddress) {
        this.invoiceShipAddress = invoiceShipAddress;
    }

    public double getInvoiceTotalAmount() {
        this.invoiceTotalAmount = calculateTotalAmount();
        return invoiceTotalAmount;
    }

    public double calculateTotalAmount() {
        return invoiceItems.stream()
                           .mapToDouble(InvoiceItem::calculateInvoiceItemSubtotal)
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
        return "Invoice [id=" + id + ", customer=" + invoiceCustomerName + ", email=" + invoiceCustomerEmail + ", billAddress=" + invoiceBillAddress
                + ", shipAddress=" + invoiceShipAddress + ", totalAmount=" + calculateTotalAmount() + "]";
    }

}
