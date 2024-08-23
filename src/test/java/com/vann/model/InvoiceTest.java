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
    private List<InvoiceItem> invoiceItems;

    @BeforeEach
    public void setUp() {
        customer = new Customer("John Doe", "john.doe@example.com");

        // Mock InvoiceItem objects
        InvoiceItem item1 = Mockito.mock(InvoiceItem.class);
        Mockito.when(item1.getPrice()).thenReturn(100.0);
        Mockito.when(item1.getQuantity()).thenReturn(2);

        InvoiceItem item2 = Mockito.mock(InvoiceItem.class);
        Mockito.when(item2.getPrice()).thenReturn(50.0);
        Mockito.when(item2.getQuantity()).thenReturn(1);

        invoiceItems = Arrays.asList(item1, item2);

        invoice = new Invoice(customer, "123 Main St", "456 Elm St", invoiceItems, 0.0);
    }

    @Test
    public void testInvoiceDefaultConstructor() {
        Invoice defaultInvoice = new Invoice();
        defaultInvoice.generateIdIfAbsent();
        assertNotNull(defaultInvoice.getInvoiceId(), "ID should be generated");
        assertNull(defaultInvoice.getCustomer(), "Customer should be null");
        assertNull(defaultInvoice.getBillAddress(), "Billing address should be null");
        assertNull(defaultInvoice.getShipAddress(), "Shipping address should be null");
        assertEquals(0.0, defaultInvoice.getTotalAmount(), "Total amount should be 0.0");
        assertTrue(defaultInvoice.getInvoiceItems().isEmpty(), "Invoice items should be empty");
    }

    @Test
    public void testInvoiceParameterizedConstructor() {
        assertNotNull(invoice.getInvoiceId(), "ID should be generated");
        assertEquals(customer, invoice.getCustomer(), "Customer should match");
        assertEquals("123 Main St", invoice.getBillAddress(), "Billing address should match");
        assertEquals("456 Elm St", invoice.getShipAddress(), "Shipping address should match");
        assertEquals(invoiceItems, invoice.getInvoiceItems(), "Invoice items should match");
    }

    @Test
    public void testCalculateTotalAmount() {
        double totalAmount = invoice.calculateTotalAmount();
        assertEquals(250.0, totalAmount, "Total amount should be the sum of the item prices times their quantities");
    }

    @Test
    public void testSetInvoiceItems() {
        InvoiceItem item3 = Mockito.mock(InvoiceItem.class);
        Mockito.when(item3.getPrice()).thenReturn(200.0);
        Mockito.when(item3.getQuantity()).thenReturn(1);

        List<InvoiceItem> newItems = Arrays.asList(item3);
        invoice.setInvoiceItems(newItems);

        assertEquals(newItems, invoice.getInvoiceItems(), "Invoice items should match the new list");
        assertEquals(200.0, invoice.getTotalAmount(), "Total amount should be recalculated based on the new items");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newId = UUID.randomUUID();
        invoice.setInvoiceId(newId);
        Customer newCustomer = new Customer("Jane Doe", "jane.doe@example.com");
        invoice.setCustomer(newCustomer);
        invoice.setBillAddress("789 Pine St");
        invoice.setShipAddress("321 Oak St");
    
        InvoiceItem item1 = Mockito.mock(InvoiceItem.class);
        Mockito.when(item1.getPrice()).thenReturn(150.0);
        Mockito.when(item1.getQuantity()).thenReturn(1);
    
        InvoiceItem item2 = Mockito.mock(InvoiceItem.class);
        Mockito.when(item2.getPrice()).thenReturn(75.0);
        Mockito.when(item2.getQuantity()).thenReturn(2);
    
        List<InvoiceItem> newInvoiceItems = Arrays.asList(item1, item2);
        invoice.setInvoiceItems(newInvoiceItems);
    
        assertEquals(newId, invoice.getInvoiceId(), "ID should match");
        assertEquals(newCustomer, invoice.getCustomer(), "Customer should match");
        assertEquals("789 Pine St", invoice.getBillAddress(), "Billing address should match");
        assertEquals("321 Oak St", invoice.getShipAddress(), "Shipping address should match");
        assertEquals(newInvoiceItems, invoice.getInvoiceItems(), "Invoice items should match");
        assertEquals(300.0, invoice.getTotalAmount(), "Total amount should match the sum of item prices times quantities");
    }

    @Test
    public void testToString() {
        String expectedString = "Invoice [id=" + invoice.getInvoiceId() +
                ", customer=" + customer + 
                ", billAddress=123 Main St, shipAddress=456 Elm St, totalAmount=0.0]";
    
        assertEquals(expectedString, invoice.toString(), "toString should match the expected output");
    }

}
