package com.vann.models;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;


public class InvoiceTest {

    private Invoice invoice;
    private Customer customer;
    private String billAddress;
    private String shipAddress;
    private List<InvoiceItem> invoiceItems;

    @BeforeEach
    public void setUp() {
        customer = mock(Customer.class);
        billAddress = "123 Main St";
        shipAddress = "456 Elm St";

        InvoiceItem item1 = Mockito.mock(InvoiceItem.class);
        Mockito.when(item1.calculateInvoiceItemSubtotal()).thenReturn(200.0);
        InvoiceItem item2 = Mockito.mock(InvoiceItem.class);
        Mockito.when(item2.calculateInvoiceItemSubtotal()).thenReturn(50.0);

        invoiceItems = Arrays.asList(item1, item2);
        invoice = new Invoice(customer, billAddress, shipAddress);
        invoice.setInvoiceItems(invoiceItems);
    }

    @Test
    public void testInvoiceDefaultConstructor() {
        Invoice defaultInvoice = new Invoice();
        assertNull(defaultInvoice.getCustomer(), "Customer should be null");
        assertNull(defaultInvoice.getShippingAddress(), "Shipping address should be null");
        assertEquals(0.0, defaultInvoice.getTotalAmount(), "Total amount should be 0.0");
        assertTrue(defaultInvoice.getInvoiceItems().isEmpty(), "Invoice items should be empty");
    }

    @Test
    public void testInvoiceParameterizedConstructor() {
        assertEquals(customer.getName(), invoice.getCustomer().getName(), "Customer name should match");
        assertEquals(customer.getEmail(), invoice.getCustomer().getEmail(), "Customer email should match");
        assertEquals("123 Main St", invoice.getBillingAddress(), "Billing address should match");
        assertEquals("456 Elm St", invoice.getShippingAddress(), "Shipping address should match");
        assertEquals(invoiceItems, invoice.getInvoiceItems(), "Invoice items should match");
    }

    @Test
    public void testSettersAndGetters() {
        Customer newCustomer = new Customer("Jane Doe", "jane.doe@example.com");
        invoice.setCustomer(newCustomer);
        invoice.setBillingAddress("789 Pine St");
        invoice.setShippingAddress("321 Oak St");

        InvoiceItem item1 = Mockito.mock(InvoiceItem.class);
        Mockito.when(item1.calculateInvoiceItemSubtotal()).thenReturn(150.0);

        InvoiceItem item2 = Mockito.mock(InvoiceItem.class);
        Mockito.when(item2.calculateInvoiceItemSubtotal()).thenReturn(75.0);

        List<InvoiceItem> newInvoiceItems = Arrays.asList(item1, item2);
        invoice.setInvoiceItems(newInvoiceItems);

        assertEquals(newCustomer, invoice.getCustomer(), "Customer should match");
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

}
