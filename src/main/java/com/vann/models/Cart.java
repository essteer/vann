package com.vann.models;

import java.util.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carts")
@Getter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ElementCollection
    @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Integer> cartItems = new HashMap<>();

    public Cart() {
    }

    public Cart(User user, Map<UUID, Integer> cartItems) {
        this.user = user;
        this.cartItems = cartItems;
    }

    public Map<UUID, Integer> getCartItems() {
        if (cartItems == null) {
            this.cartItems = new HashMap<>();
        }
        return cartItems;
    }

    public void setCartItems(Map<UUID, Integer> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public String toString() {
        return "Cart(id=" + id + ", userId=" + user.getId() + ")";
    }

}
