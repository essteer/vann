package com.vann.models;

import java.util.*;
import java.util.regex.Pattern;

import com.vann.utils.LogHandler;

import jakarta.persistence.*;

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
        try {
            generateIdIfAbsent();
            setInvoiceCustomerId(customerId);
            setInvoiceCustomerName(customerName);
            setInvoiceCustomerEmail(customerEmail);
            this.invoiceBillAddress = billAddress;
            this.invoiceShipAddress = shipAddress;
            LogHandler.createInstanceOK(Invoice.class, this.id, customerName, customerEmail, billAddress, shipAddress);
        } catch (Exception e) {
            LogHandler.createInstanceError(Invoice.class, customerName, customerEmail, billAddress, shipAddress);
            throw e;
        }
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
        if (id == null) {
            LogHandler.nullAttributeWarning(Invoice.class, getInvoiceId(), "id");
        } else {
            this.id = id;
            LogHandler.validAttributeOK(Invoice.class, getInvoiceId(), "id", String.valueOf(getInvoiceId()));
        }
    }

    public UUID getInvoiceCustomerId() {
        return invoiceCustomerId;
    }

    public void setInvoiceCustomerId(UUID invoiceCustomerId) {
        if (invoiceCustomerId == null) {
            LogHandler.nullAttributeWarning(Invoice.class, getInvoiceId(), "invoiceCustomerId");
        } else {
            this.invoiceCustomerId = invoiceCustomerId;
            LogHandler.validAttributeOK(Invoice.class, getInvoiceId(), "invoiceCustomerId", String.valueOf(invoiceCustomerId));
        }
    }

    public String getInvoiceCustomerName() {
        return invoiceCustomerName;
    }

    public void setInvoiceCustomerName(String invoiceCustomerName) {
        if (invoiceCustomerName == null || invoiceCustomerName.trim().isEmpty()) {
            LogHandler.nullAttributeWarning(Invoice.class, getInvoiceId(), "invoiceCustomerName");
        } else {
            this.invoiceCustomerName = invoiceCustomerName;
            LogHandler.validAttributeOK(Invoice.class, getInvoiceId(), "invoiceCustomerName", invoiceCustomerName);
        }
    }

    public String getInvoiceCustomerEmail() {
        return invoiceCustomerEmail;
    }

    public void setInvoiceCustomerEmail(String invoiceCustomerEmail) {
        String email = invoiceCustomerEmail.trim().toLowerCase();
        try {
            if (isValidEmail(email)) {
                this.invoiceCustomerEmail = email;
                LogHandler.validAttributeOK(Invoice.class, getInvoiceId(), "email", getInvoiceCustomerEmail());
            }
        } catch (Exception e) {
            LogHandler.invalidAttributeError(Invoice.class, getInvoiceId(), "email", invoiceCustomerEmail, e.getMessage());
            throw e;
        }
    }

    private boolean isValidEmail(String email) throws IllegalArgumentException {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        if (email == null || email.trim().isEmpty()) {
            LogHandler.nullAttributeWarning(Invoice.class, getInvoiceId(), "email");
            return false;
        } else if (!pattern.matcher(email).matches()) {
            LogHandler.invalidAttributeError(Invoice.class, getInvoiceId(), "email", email, "Invalid email format");
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        return true;
    }

    public String getInvoiceBillAddress() {
        return invoiceBillAddress;
    }

    public void setInvoiceBillAddress(String invoiceBillAddress) {
        if (invoiceBillAddress == null) {
            LogHandler.nullAttributeWarning(Invoice.class, getInvoiceId(), "invoiceBillAddress");
        } else {
            this.invoiceBillAddress = invoiceBillAddress;
            LogHandler.validAttributeOK(Invoice.class, getInvoiceId(), "invoiceBillAddress", invoiceBillAddress);
        }
    }

    public String getInvoiceShipAddress() {
        return invoiceShipAddress;
    }

    public void setInvoiceShipAddress(String invoiceShipAddress) {
        if (invoiceShipAddress == null) {
            this.invoiceShipAddress = getInvoiceBillAddress();
            LogHandler.nullAttributeWarning(Invoice.class, getInvoiceId(), "invoiceShipAddress");
        } else {
            this.invoiceShipAddress = invoiceShipAddress;
            LogHandler.validAttributeOK(Invoice.class, getInvoiceId(), "invoiceShipAddress", invoiceShipAddress);
        }
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
