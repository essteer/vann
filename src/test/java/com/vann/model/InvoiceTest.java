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
    private UUID customerId;
    private String customerName;
    private String customerEmail;
    private String billAddress;
    private String shipAddress;
    private List<InvoiceItem> invoiceItems;

    @BeforeEach
    public void setUp() {
        customerId = UUID.randomUUID();
        customerName = "John Doe";
        customerEmail = "john.doe@example.com";
        billAddress = "123 Main St";
        shipAddress = "456 Elm St";

        InvoiceItem item1 = Mockito.mock(InvoiceItem.class);
        Mockito.when(item1.calculateInvoiceItemSubtotal()).thenReturn(200.0);
        InvoiceItem item2 = Mockito.mock(InvoiceItem.class);
        Mockito.when(item2.calculateInvoiceItemSubtotal()).thenReturn(50.0);

        invoiceItems = Arrays.asList(item1, item2);
        invoice = new Invoice(customerId, customerName, customerEmail, billAddress, shipAddress);
        invoice.setInvoiceItems(invoiceItems);
    }

    @Test
    public void testInvoiceDefaultConstructor() {
        Invoice defaultInvoice = new Invoice();
        defaultInvoice.generateIdIfAbsent();
        assertNotNull(defaultInvoice.getInvoiceId(), "ID should be generated");
        assertNull(defaultInvoice.getInvoiceCustomerId(), "Customer ID should be null");
        assertNull(defaultInvoice.getInvoiceCustomerName(), "Customer name should be null");
        assertNull(defaultInvoice.getInvoiceCustomerEmail(), "Customer email should be null");
        assertNull(defaultInvoice.getInvoiceBillAddress(), "Billing address should be null");
        assertNull(defaultInvoice.getInvoiceShipAddress(), "Shipping address should be null");
        assertEquals(0.0, defaultInvoice.getInvoiceTotalAmount(), "Total amount should be 0.0");
        assertTrue(defaultInvoice.getInvoiceItems().isEmpty(), "Invoice items should be empty");
    }

    @Test
    public void testInvoiceParameterizedConstructor() {
        assertNotNull(invoice.getInvoiceId(), "ID should be generated");
        assertEquals(customerId, invoice.getInvoiceCustomerId(), "Customer ID should match");
        assertEquals(customerName, invoice.getInvoiceCustomerName(), "Customer name should match");
        assertEquals(customerEmail, invoice.getInvoiceCustomerEmail(), "Customer email should match");
        assertEquals("123 Main St", invoice.getInvoiceBillAddress(), "Billing address should match");
        assertEquals("456 Elm St", invoice.getInvoiceShipAddress(), "Shipping address should match");
        assertEquals(invoiceItems, invoice.getInvoiceItems(), "Invoice items should match");
    }

    @Test
    public void testCalculateTotalAmount() {
        double totalAmount = invoice.calculateTotalAmount();
        assertEquals(250.0, totalAmount, "Total amount should be the sum of the item subtotals");
    }

    @Test
    public void testSetInvoiceItems() {
        InvoiceItem item3 = Mockito.mock(InvoiceItem.class);
        Mockito.when(item3.calculateInvoiceItemSubtotal()).thenReturn(300.0);

        List<InvoiceItem> newItems = Arrays.asList(item3);
        invoice.setInvoiceItems(newItems);

        assertEquals(newItems, invoice.getInvoiceItems(), "Invoice items should match the new list");
        assertEquals(300.0, invoice.getInvoiceTotalAmount(), "Total amount should be recalculated based on the new items");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newId = UUID.randomUUID();
        invoice.setInvoiceId(newId);
        invoice.setInvoiceCustomerId(UUID.randomUUID());
        invoice.setInvoiceCustomerName("Jane Doe");
        invoice.setInvoiceCustomerEmail("jane.doe@example.com");
        invoice.setInvoiceBillAddress("789 Pine St");
        invoice.setInvoiceShipAddress("321 Oak St");

        InvoiceItem item1 = Mockito.mock(InvoiceItem.class);
        Mockito.when(item1.calculateInvoiceItemSubtotal()).thenReturn(150.0);

        InvoiceItem item2 = Mockito.mock(InvoiceItem.class);
        Mockito.when(item2.calculateInvoiceItemSubtotal()).thenReturn(75.0);

        List<InvoiceItem> newInvoiceItems = Arrays.asList(item1, item2);
        invoice.setInvoiceItems(newInvoiceItems);

        assertEquals(newId, invoice.getInvoiceId(), "ID should match");
        assertEquals("Jane Doe", invoice.getInvoiceCustomerName(), "Customer name should match");
        assertEquals("jane.doe@example.com", invoice.getInvoiceCustomerEmail(), "Customer email should match");
        assertEquals("789 Pine St", invoice.getInvoiceBillAddress(), "Billing address should match");
        assertEquals("321 Oak St", invoice.getInvoiceShipAddress(), "Shipping address should match");
        assertEquals(newInvoiceItems, invoice.getInvoiceItems(), "Invoice items should match");
        assertEquals(225.0, invoice.getInvoiceTotalAmount(), "Total amount should match the sum of item subtotals");
    }

    @Test
    public void testToString() {
        String expectedString = "Invoice [id=" + invoice.getInvoiceId() +
                ", customer=" + customerName + 
                ", email=" + customerEmail + 
                ", billAddress=123 Main St, shipAddress=456 Elm St, totalAmount=250.0]";
    
        assertEquals(expectedString, invoice.toString(), "toString should match the expected output");
    }
}
