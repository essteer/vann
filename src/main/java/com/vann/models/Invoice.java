package com.vann.models;

import java.util.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    private UUID id;

    private UUID customerId;
    private String customerName;

    @Email
    private String customerEmail;

    private String billingAddress;
    private String shippingAddress;

    private double totalAmount;

    @OneToMany(cascade = CascadeType.ALL)
    private List<InvoiceItem> invoiceItems = new ArrayList<>();

    @Column(nullable = false, updatable = false)
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date creationDate;

    public Invoice() {
    }

    public Invoice(UUID customerId, String customerName, String customerEmail, String billingAddress, String shippingAddress) {
        generateIdIfAbsent();
        setCustomerId(customerId);
        setCustomerName(customerName);
        setCustomerEmail(customerEmail);
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
    }

    public Invoice(UUID customerId, String customerName, String customerEmail, String billingAddress) {
        this(customerId, customerName, customerEmail, billingAddress, billingAddress);
    }

    public void generateIdIfAbsent() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getId() {
        generateIdIfAbsent();
        return id;
    }

    public void setId(UUID id) {
        if (id != null) {
            this.id = id;
        }
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        if (customerId != null) {
            this.customerId = customerId;
        }
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        if (customerName != null && !(customerName.trim().isEmpty())) {
            this.customerName = customerName;
        }
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String email) {
        String formattedEmail = email.trim().toLowerCase();
        if (formattedEmail != null && !(formattedEmail.trim().isEmpty())) {
            this.customerEmail = formattedEmail;
        }
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        if (billingAddress != null) {
            this.billingAddress = billingAddress;
        }
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        if (shippingAddress == null) {
            this.shippingAddress = getBillingAddress();
        } else {
            this.shippingAddress = shippingAddress;
        }
    }

    public double getTotalAmount() {
        this.totalAmount = calculateTotalAmount();
        return totalAmount;
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
        this.totalAmount = calculateTotalAmount();
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    @Override
    public String toString() {
        return "Invoice [id=" + id + ", customer=" + customerName + ", email=" + customerEmail + ", billingAddress=" + billingAddress
                + ", shippingAddress=" + shippingAddress + ", totalAmount=" + calculateTotalAmount() + "]";
    }

}
