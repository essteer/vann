package com.vann.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
        generateIdIfAbsent();
        this.customerName = name;
        this.customerEmail = email.toLowerCase();
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
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail.toLowerCase();
    }

    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + customerName + ", email="
                + customerEmail + "]";
    }

}
