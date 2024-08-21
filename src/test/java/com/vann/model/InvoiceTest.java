package com.vann.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceTest {

    private Invoice invoice;
    private Customer customer;
    private List<InvoiceProduct> invoiceProducts;

    @BeforeEach
    public void setUp() {
        customer = new Customer("John Doe", "john.doe@example.com");

        // Mock InvoiceProduct objects
        InvoiceProduct product1 = Mockito.mock(InvoiceProduct.class);
        Mockito.when(product1.getPrice()).thenReturn(100.0);
        Mockito.when(product1.getQuantity()).thenReturn(2);

        InvoiceProduct product2 = Mockito.mock(InvoiceProduct.class);
        Mockito.when(product2.getPrice()).thenReturn(50.0);
        Mockito.when(product2.getQuantity()).thenReturn(1);

        invoiceProducts = Arrays.asList(product1, product2);

        invoice = new Invoice(customer, "123 Main St", "456 Elm St", invoiceProducts, 0.0);
    }

    @Test
    public void testInvoiceDefaultConstructor() {
        Invoice defaultInvoice = new Invoice();
        assertNotNull(defaultInvoice.getInvoiceUuid(), "UUID should be generated");
        assertNull(defaultInvoice.getCustomer(), "Customer should be null");
        assertNull(defaultInvoice.getBillingAddress(), "Billing address should be null");
        assertNull(defaultInvoice.getShippingAddress(), "Shipping address should be null");
        assertEquals(0.0, defaultInvoice.getTotalAmount(), "Total amount should be 0.0");
        assertNull(defaultInvoice.getInvoiceProducts(), "Invoice products should be null");
    }

    @Test
    public void testInvoiceParameterizedConstructor() {
        assertNotNull(invoice.getInvoiceUuid(), "UUID should be generated");
        assertEquals(customer, invoice.getCustomer(), "Customer should match");
        assertEquals("123 Main St", invoice.getBillingAddress(), "Billing address should match");
        assertEquals("456 Elm St", invoice.getShippingAddress(), "Shipping address should match");
        assertEquals(invoiceProducts, invoice.getInvoiceProducts(), "Invoice products should match");
    }

    @Test
    public void testCalculateTotalAmount() {
        double totalAmount = invoice.calculateTotalAmount();
        assertEquals(250.0, totalAmount, "Total amount should be the sum of the product prices times their quantities");
    }

    @Test
    public void testSetInvoiceProducts() {
        InvoiceProduct product3 = Mockito.mock(InvoiceProduct.class);
        Mockito.when(product3.getPrice()).thenReturn(200.0);
        Mockito.when(product3.getQuantity()).thenReturn(1);

        List<InvoiceProduct> newProducts = Arrays.asList(product3);
        invoice.setInvoiceProducts(newProducts);

        assertEquals(newProducts, invoice.getInvoiceProducts(), "Invoice products should match the new list");
        assertEquals(200.0, invoice.getTotalAmount(), "Total amount should be recalculated based on the new products");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newUuid = UUID.randomUUID();
        invoice.setInvoiceUuid(newUuid);
        Customer newCustomer = new Customer("Jane Doe", "jane.doe@example.com");
        invoice.setCustomer(newCustomer);
        invoice.setBillingAddress("789 Pine St");
        invoice.setShippingAddress("321 Oak St");
    
        InvoiceProduct product1 = Mockito.mock(InvoiceProduct.class);
        Mockito.when(product1.getPrice()).thenReturn(150.0);
        Mockito.when(product1.getQuantity()).thenReturn(1);
    
        InvoiceProduct product2 = Mockito.mock(InvoiceProduct.class);
        Mockito.when(product2.getPrice()).thenReturn(75.0);
        Mockito.when(product2.getQuantity()).thenReturn(2);
    
        List<InvoiceProduct> newInvoiceProducts = Arrays.asList(product1, product2);
        invoice.setInvoiceProducts(newInvoiceProducts);
    
        assertEquals(newUuid, invoice.getInvoiceUuid(), "UUID should match");
        assertEquals(newCustomer, invoice.getCustomer(), "Customer should match");
        assertEquals("789 Pine St", invoice.getBillingAddress(), "Billing address should match");
        assertEquals("321 Oak St", invoice.getShippingAddress(), "Shipping address should match");
        assertEquals(newInvoiceProducts, invoice.getInvoiceProducts(), "Invoice products should match");
        assertEquals(300.0, invoice.getTotalAmount(), "Total amount should match the sum of product prices times quantities");
    }

    @Test
    public void testToString() {
        String expectedString = "Invoice [invoiceUuid=" + invoice.getInvoiceUuid() +
                ", customer=" + customer + 
                ", billingAddress=123 Main St, shippingAddress=456 Elm St, totalAmount=0.0, invoiceProducts=" +
                invoiceProducts + "]";
    
        assertEquals(expectedString, invoice.toString(), "toString should match the expected output");
    }

}
