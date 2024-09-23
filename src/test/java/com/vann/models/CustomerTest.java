package com.vann.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;


public class CustomerTest {

    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer("John Doe", "john.doe@example.com");
    }

    @Test
    public void testCustomerDefaultConstructor() {
        Customer defaultCustomer = new Customer();
        assertNull(defaultCustomer.getName(), "Customer name should be null");
        assertNull(defaultCustomer.getEmail(), "Customer email should be null");
    }

    @Test
    public void testCustomerParameterizedConstructor() {
        assertEquals("John Doe", customer.getName(), "Customer name should match");
        assertEquals("john.doe@example.com", customer.getEmail(), "Customer email should match");
    }

    @Test
    public void testSettersAndGetters() {
        customer.setName("Jane Doe");
        customer.setEmail("jane.doe@example.com");

        assertEquals("Jane Doe", customer.getName(), "Customer name should match");
        assertEquals("jane.doe@example.com", customer.getEmail(), "Customer email should match");
    }

    @Test
    public void testToString() {
        String expectedString = "Customer [id=" + customer.getId() + 
                                ", name=John Doe, email=john.doe@example.com]";
        assertEquals(expectedString, customer.toString(), "toString should match");
    }
}
