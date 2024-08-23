package com.vann.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer("John Doe", "john.doe@example.com");
    }

    @Test
    public void testCustomerDefaultConstructor() {
        Customer defaultCustomer = new Customer();
        defaultCustomer.generateIdIfAbsent();
        assertNotNull(defaultCustomer.getCustomerId(), "UUID should be generated");
        assertNull(defaultCustomer.getCustomerName(), "Customer name should be null");
        assertNull(defaultCustomer.getCustomerEmail(), "Customer email should be null");
    }

    @Test
    public void testCustomerParameterizedConstructor() {
        assertNotNull(customer.getCustomerId(), "UUID should be generated");
        assertEquals("John Doe", customer.getCustomerName(), "Customer name should match");
        assertEquals("john.doe@example.com", customer.getCustomerEmail(), "Customer email should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newUuid = UUID.randomUUID();
        customer.setCustomerId(newUuid);
        customer.setCustomerName("Jane Doe");
        customer.setCustomerEmail("jane.doe@example.com");

        assertEquals(newUuid, customer.getCustomerId(), "UUID should match");
        assertEquals("Jane Doe", customer.getCustomerName(), "Customer name should match");
        assertEquals("jane.doe@example.com", customer.getCustomerEmail(), "Customer email should match");
    }

    @Test
    public void testToString() {
        String expectedString = "Customer [id=" + customer.getCustomerId() + 
                                ", name=John Doe, email=john.doe@example.com]";
        assertEquals(expectedString, customer.toString(), "toString should match");
    }
}
