package com.vann.models;

import java.util.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "customers")
public class Customer {

    @Id
    private UUID id;

    private String name;

    @Column(unique = true)
    @Email
    private String email;

    @Column(nullable = false, updatable = false)
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date creationDate;

    public Customer() {
    }

    public Customer(String name, String email) {
        generateIdIfAbsent();
        setName(name);
        setEmail(email);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !(name.trim().isEmpty())) {
            this.name = name;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String formattedEmail = email.trim().toLowerCase();
        if (formattedEmail != null && !(formattedEmail.trim().isEmpty())) {
            this.email = formattedEmail;
        }
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + name + ", email="
                + email + "]";
    }

}
