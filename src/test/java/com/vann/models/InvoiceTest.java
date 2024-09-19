package com.vann.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;


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
        assertNotNull(defaultInvoice.getId(), "ID should be generated");
        assertNull(defaultInvoice.getCustomerId(), "Customer ID should be null");
        assertNull(defaultInvoice.getCustomerName(), "Customer name should be null");
        assertNull(defaultInvoice.getCustomerEmail(), "Customer email should be null");
        assertNull(defaultInvoice.getBillingAddress(), "Billing address should be null");
        assertNull(defaultInvoice.getShippingAddress(), "Shipping address should be null");
        assertEquals(0.0, defaultInvoice.getTotalAmount(), "Total amount should be 0.0");
        assertTrue(defaultInvoice.getInvoiceItems().isEmpty(), "Invoice items should be empty");
    }

    @Test
    public void testInvoiceParameterizedConstructor() {
        assertNotNull(invoice.getId(), "ID should be generated");
        assertEquals(customerId, invoice.getCustomerId(), "Customer ID should match");
        assertEquals(customerName, invoice.getCustomerName(), "Customer name should match");
        assertEquals(customerEmail, invoice.getCustomerEmail(), "Customer email should match");
        assertEquals("123 Main St", invoice.getBillingAddress(), "Billing address should match");
        assertEquals("456 Elm St", invoice.getShippingAddress(), "Shipping address should match");
        assertEquals(invoiceItems, invoice.getInvoiceItems(), "Invoice items should match");
    }

    @Test
    public void testGenerateIdIfAbsent() {
        Invoice invoice = new Invoice();
        invoice.generateIdIfAbsent();
        assertNotNull(invoice.getId(), "UUID should be generated");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newId = UUID.randomUUID();
        invoice.setId(newId);
        invoice.setCustomerId(UUID.randomUUID());
        invoice.setCustomerName("Jane Doe");
        invoice.setCustomerEmail("jane.doe@example.com");
        invoice.setBillingAddress("789 Pine St");
        invoice.setShippingAddress("321 Oak St");

        InvoiceItem item1 = Mockito.mock(InvoiceItem.class);
        Mockito.when(item1.calculateInvoiceItemSubtotal()).thenReturn(150.0);

        InvoiceItem item2 = Mockito.mock(InvoiceItem.class);
        Mockito.when(item2.calculateInvoiceItemSubtotal()).thenReturn(75.0);

        List<InvoiceItem> newInvoiceItems = Arrays.asList(item1, item2);
        invoice.setInvoiceItems(newInvoiceItems);

        assertEquals(newId, invoice.getId(), "ID should match");
        assertEquals("Jane Doe", invoice.getCustomerName(), "Customer name should match");
        assertEquals("jane.doe@example.com", invoice.getCustomerEmail(), "Customer email should match");
        assertEquals("789 Pine St", invoice.getBillingAddress(), "Billing address should match");
        assertEquals("321 Oak St", invoice.getShippingAddress(), "Shipping address should match");
        assertEquals(newInvoiceItems, invoice.getInvoiceItems(), "Invoice items should match");
        assertEquals(225.0, invoice.getTotalAmount(), "Total amount should match the sum of item subtotals");
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
        assertEquals(300.0, invoice.getTotalAmount(), "Total amount should be recalculated based on the new items");
    }

    @Test
    public void testToString() {
        String expectedString = "Invoice [id=" + invoice.getId() +
                ", customer=" + customerName + 
                ", email=" + customerEmail + 
                ", billingAddress=123 Main St, shippingAddress=456 Elm St, totalAmount=250.0]";
    
        assertEquals(expectedString, invoice.toString(), "toString should match the expected output");
    }
}
