package com.vann.models;

import java.util.*;
import java.util.regex.Pattern;

import com.vann.utils.LogHandler;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

@Entity
public class Customer {

    @Id
    private UUID id;

    private String customerName;

    @Column(unique = true)
    @Email
    private String customerEmail;

    public Customer() {
    }

    public Customer(String name, String email) {
        try {
            generateIdIfAbsent();
            setCustomerName(name);
            setCustomerEmail(email);
            LogHandler.createInstanceOK(Customer.class, this.id, name, email);
        } catch (Exception e) {
            LogHandler.createInstanceError(Customer.class, name, email);
            throw e;
        }
    }

    public void generateIdIfAbsent() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getCustomerId() {
        generateIdIfAbsent();
        return id;
    }

    public void setCustomerId(UUID id) {
        if (id == null) {
            LogHandler.nullAttributeWarning(Customer.class, getCustomerId(), "id");
        } else {
            this.id = id;
            LogHandler.validAttributeOK(Customer.class, getCustomerId(), "id", String.valueOf(getCustomerId()));
        }
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            LogHandler.nullAttributeWarning(Customer.class, getCustomerId(), "name");
        } else {
            this.customerName = customerName;
            LogHandler.validAttributeOK(Customer.class, getCustomerId(), "name", getCustomerName());
        }
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) throws IllegalArgumentException {
        String email = customerEmail.trim().toLowerCase();
        try {
            if (isValidEmail(email)) {
                this.customerEmail = email;
                LogHandler.validAttributeOK(Customer.class, getCustomerId(), "email", getCustomerEmail());
            }
        } catch (Exception e) {
            LogHandler.invalidAttributeError(Customer.class, getCustomerId(), "email", customerEmail, e.getMessage());
            throw e;
        }
    }

    private boolean isValidEmail(String email) throws IllegalArgumentException {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        if (email == null || email.trim().isEmpty()) {
            LogHandler.nullAttributeWarning(Customer.class, getCustomerId(), "email");
            return false;
        } else if (!pattern.matcher(email).matches()) {
            LogHandler.invalidAttributeError(Customer.class, getCustomerId(), "email", email, "Invalid email format");
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        return true;
    }

    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + customerName + ", email="
                + customerEmail + "]";
    }

}
