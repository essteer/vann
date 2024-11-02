package com.vann.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.util.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "users")
@Getter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, length = 100)
    @Email
    private String email;

    @Column(nullable = false, updatable = false)
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date creationDate;

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void setName(String name) {
        if (name != null && !(name.trim().isEmpty())) {
            this.name = name;
        }
    }

    public void setEmail(String email) {
        String formattedEmail = email.trim().toLowerCase();
        if (formattedEmail != null && !(formattedEmail.trim().isEmpty())) {
            this.email = formattedEmail;
        }
    }

}
