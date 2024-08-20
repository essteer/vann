package com.vann.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Customer {

    @Id
    private UUID customerUuid;

    private String customerName;

    @Column(unique = true)
    private String customerEmail;

    public Customer() {
        this.customerUuid = UUID.randomUUID();
    }

    public Customer(String name, String email) {
        this();
        this.customerName = name;
        this.customerEmail = email;

    }

    public UUID getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(UUID customerUuid) {
        this.customerUuid = customerUuid;
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
        this.customerEmail = customerEmail;
    }

    @Override
    public String toString() {
        return "Customer [customerUuid=" + customerUuid + ", customerName=" + customerName + ", customerEmail="
                + customerEmail + "]";
    }

}
