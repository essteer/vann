package com.vann.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.UUID;


public class CustomerTest {

    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer("John Doe", "john.doe@example.com");
    }

    @Test
    public void testCustomerDefaultConstructor() {
        Customer defaultCustomer = new Customer();
        assertNotNull(defaultCustomer.getId(), "UUID should be generated");
        assertNull(defaultCustomer.getName(), "Customer name should be null");
        assertNull(defaultCustomer.getEmail(), "Customer email should be null");
    }

    @Test
    public void testCustomerParameterizedConstructor() {
        assertNotNull(customer.getId(), "UUID should be generated");
        assertEquals("John Doe", customer.getName(), "Customer name should match");
        assertEquals("john.doe@example.com", customer.getEmail(), "Customer email should match");
    }

    @Test
    public void testGenerateIdIfAbsent() {
        Customer customer = new Customer();
        customer.generateIdIfAbsent();
        assertNotNull(customer.getId(), "UUID should be generated");
    }

    @Test
    public void testEmptyNameAndEmail() {
        Customer customer = new Customer("", "");
        assertNotNull(customer.getId(), "UUID should be generated");
        assertNull(customer.getName(), "Empty customer name should be ignored (stays null)");
        assertNull(customer.getEmail(), "Empty Customer email should be ignored (stays null)");
    }

    @Test
    public void testEmailNormalisation() {
        Customer customer = new Customer("Jane Doe", "JANE.DOE@EXAMPLE.COM");
        assertEquals("jane.doe@example.com", customer.getEmail(), "Email should be normalised to lowercase");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newUuid = UUID.randomUUID();
        customer.setId(newUuid);
        customer.setName("Jane Doe");
        customer.setEmail("jane.doe@example.com");

        assertEquals(newUuid, customer.getId(), "UUID should match");
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
