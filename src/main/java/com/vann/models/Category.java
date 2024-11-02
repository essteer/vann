package com.vann.models;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;
import com.vann.models.enums.CategoryType;

@Entity
@Table(name = "categories")
@Getter
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private CategoryType type;

    @Column(unique = true, nullable = false)
    private String name;

    public Category() {
    }

    public Category(CategoryType type, String name) {
        this.type = type;
        this.name = name;
    }

    public void setType(CategoryType type) {
        if (type != null) {
            this.type = type;
        }
    }

    public void setName(String name) {
        if (name != null && !(name.trim().isEmpty())) {
            this.name = name.toLowerCase();
        }
    }

}
