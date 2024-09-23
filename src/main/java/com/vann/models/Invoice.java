package com.vann.models;

import java.util.*;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "fk_customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
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

    public Invoice(Customer customer, String billingAddress, String shippingAddress) {
        this.customer = customer;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
    }

    public Invoice(Customer customer, String billingAddress) {
        this(customer, billingAddress, billingAddress);
    }

    public UUID getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        if (customer != null) {
            this.customer = customer;
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
        return "Invoice [id=" + id + ", customerId=" + customer.getId() + ", email=" + customer.getEmail() + ", billingAddress=" + billingAddress
                + ", shippingAddress=" + shippingAddress + ", totalAmount=" + calculateTotalAmount() + "]";
    }

}
