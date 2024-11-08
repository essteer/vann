package com.vann.models;

import java.util.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "invoices")
@Getter
@ToString
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id", nullable = false)
    private User user;

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

    public Invoice(User user, String billingAddress, String shippingAddress) {
        this.user = user;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
    }

    public Invoice(User user, String billingAddress) {
        this(user, billingAddress, billingAddress);
    }

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
        }
    }

    public void setBillingAddress(String billingAddress) {
        if (billingAddress != null) {
            this.billingAddress = billingAddress;
        }
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

}
