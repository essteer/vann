package com.vann.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.model.Customer;
import com.vann.model.Invoice;
import com.vann.model.InvoiceItem;
import com.vann.repositories.InvoiceRepo;
import com.vann.service.InvoiceItemService;
import com.vann.service.InvoiceService;


public class InvoiceServiceTest {

    @Mock
    private InvoiceRepo invoiceRepo;

    @Mock
    private InvoiceItemService invoiceItemService;

    @InjectMocks
    private InvoiceService invoiceService;

    @Mock
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllInvoices() {
        Invoice invoice1 = new Invoice(customer, "12 Oak Street", "15 Elm Street", new ArrayList<>(), 200.0);
        Invoice invoice2 = new Invoice(customer, "22 Pine Street", "25 Maple Street", new ArrayList<>(), 300.0);

        when(invoiceRepo.findAll()).thenReturn(Arrays.asList(invoice1, invoice2));

        List<Invoice> invoices = invoiceService.findAllInvoices();

        assertEquals(2, invoices.size());
        assertTrue(invoices.contains(invoice1));
        assertTrue(invoices.contains(invoice2));
        verify(invoiceRepo, times(1)).findAll();
    }

    @Test
    void testFindInvoicesByTotalAmountBetween() {
        double minAmount = 100.0;
        double maxAmount = 500.0;

        Invoice invoice1 = new Invoice(customer, "12 Oak Street", "15 Elm Street", new ArrayList<>(), 200.0);
        Invoice invoice2 = new Invoice(customer, "22 Pine Street", "25 Maple Street", new ArrayList<>(), 300.0);

        when(invoiceRepo.findByInvoiceTotalAmountBetween(minAmount, maxAmount))
            .thenReturn(Arrays.asList(invoice1, invoice2));

        List<Invoice> invoices = invoiceService.findInvoicesByTotalAmountBetween(minAmount, maxAmount);

        assertEquals(2, invoices.size());
        assertTrue(invoices.contains(invoice1));
        assertTrue(invoices.contains(invoice2));
        verify(invoiceRepo, times(1)).findByInvoiceTotalAmountBetween(minAmount, maxAmount);
    }

    @Test
    void testFindInvoicesByTotalAmountGreaterThan() {
        double amount = 150.0;

        Invoice invoice1 = new Invoice(customer, "12 Oak Street", "15 Elm Street", new ArrayList<>(), 200.0);
        Invoice invoice2 = new Invoice(customer, "22 Pine Street", "25 Maple Street", new ArrayList<>(), 300.0);

        when(invoiceRepo.findByInvoiceTotalAmountGreaterThan(amount))
            .thenReturn(Arrays.asList(invoice1, invoice2));

        List<Invoice> invoices = invoiceService.findInvoicesByTotalAmountGreaterThan(amount);

        assertEquals(2, invoices.size());
        assertTrue(invoices.contains(invoice1));
        assertTrue(invoices.contains(invoice2));
        verify(invoiceRepo, times(1)).findByInvoiceTotalAmountGreaterThan(amount);
    }

    @Test
    void testFindInvoicesByTotalAmountLessThan() {
        double amount = 250.0;

        Invoice invoice1 = new Invoice(customer, "12 Oak Street", "15 Elm Street", new ArrayList<>(), 200.0);
        Invoice invoice2 = new Invoice(customer, "22 Pine Street", "25 Maple Street", new ArrayList<>(), 150.0);

        when(invoiceRepo.findByInvoiceTotalAmountLessThan(amount))
            .thenReturn(Arrays.asList(invoice1, invoice2));

        List<Invoice> invoices = invoiceService.findInvoicesByTotalAmountLessThan(amount);

        assertEquals(2, invoices.size());
        assertTrue(invoices.contains(invoice1));
        assertTrue(invoices.contains(invoice2));
        verify(invoiceRepo, times(1)).findByInvoiceTotalAmountLessThan(amount);
    }

    @Test
    void testFindInvoicesByCustomerId() {
        UUID customerId = UUID.randomUUID();
        Invoice invoice1 = new Invoice(customer, "12 Oak Street", "15 Elm Street", new ArrayList<>(), 200.0);
        Invoice invoice2 = new Invoice(customer, "22 Pine Street", "25 Maple Street", new ArrayList<>(), 300.0);

        when(invoiceRepo.findByInvoiceCustomer_Id(customerId)).thenReturn(Arrays.asList(invoice1, invoice2));

        List<Invoice> invoices = invoiceService.findInvoicesByCustomerId(customerId);

        assertEquals(2, invoices.size());
        assertTrue(invoices.contains(invoice1));
        assertTrue(invoices.contains(invoice2));
        verify(invoiceRepo, times(1)).findByInvoiceCustomer_Id(customerId);
    }

    @Test
    void testFindInvoicesByCustomerEmail() {
        String email = "customer@example.com";
        Invoice invoice1 = new Invoice(customer, "12 Oak Street", "15 Elm Street", new ArrayList<>(), 200.0);
        Invoice invoice2 = new Invoice(customer, "22 Pine Street", "25 Maple Street", new ArrayList<>(), 300.0);

        when(invoiceRepo.findByInvoiceCustomer_CustomerEmail(email)).thenReturn(Arrays.asList(invoice1, invoice2));

        List<Invoice> invoices = invoiceService.findInvoicesByCustomerEmail(email);

        assertEquals(2, invoices.size());
        assertTrue(invoices.contains(invoice1));
        assertTrue(invoices.contains(invoice2));
        verify(invoiceRepo, times(1)).findByInvoiceCustomer_CustomerEmail(email);
    }

    @Test
    void testFindInvoiceById() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setBillAddress("43 Main Street");
        invoice.setShipAddress("44 South Street");

        when(invoiceRepo.findById(invoiceId)).thenReturn(Optional.of(invoice));

        Invoice foundInvoice = invoiceService.findInvoiceById(invoiceId);
        assertEquals("43 Main Street", foundInvoice.getBillAddress());
        assertEquals("44 South Street", foundInvoice.getShipAddress());
        verify(invoiceRepo, times(1)).findById(invoiceId);
    }

    @Test
    void testSaveInvoice() {
        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setBillAddress("10 Garden Road");
        invoice.setShipAddress("233 Hill Street");

        when(invoiceRepo.save(invoice)).thenReturn(invoice);

        Invoice savedInvoice = invoiceService.saveInvoice(invoice);

        assertNotNull(savedInvoice);
        assertEquals(customer, savedInvoice.getCustomer());
        assertEquals("10 Garden Road", savedInvoice.getBillAddress());
        assertEquals("233 Hill Street", savedInvoice.getShipAddress());
        verify(invoiceRepo, times(1)).save(invoice);
    }

    @Test
    void testUpdateInvoice_Success() {
        UUID invoiceId = UUID.randomUUID();
        Invoice existingInvoice = new Invoice(customer, "12 Oak Street", "15 Elm Street", new ArrayList<>(), 200.0);
        existingInvoice.setInvoiceId(invoiceId);

        Invoice updatedInvoice = new Invoice(customer, "22 Pine Street", "25 Maple Street", new ArrayList<>(), 300.0);
        updatedInvoice.setInvoiceId(invoiceId);

        when(invoiceRepo.existsById(invoiceId)).thenReturn(true);
        when(invoiceRepo.save(updatedInvoice)).thenReturn(updatedInvoice);

        Invoice result = invoiceService.updateInvoice(invoiceId, updatedInvoice);

        assertNotNull(result);
        assertEquals("22 Pine Street", result.getBillAddress());
        assertEquals("25 Maple Street", result.getShipAddress());
        assertEquals(300.0, result.getTotalAmount());
        verify(invoiceRepo, times(1)).existsById(invoiceId);
        verify(invoiceRepo, times(1)).save(updatedInvoice);
    }

    @Test
    void testUpdateInvoice_NotFound() {
        UUID invoiceId = UUID.randomUUID();
        Invoice updatedInvoice = new Invoice(customer, "22 Pine Street", "25 Maple Street", new ArrayList<>(), 300.0);

        when(invoiceRepo.existsById(invoiceId)).thenReturn(false);

        Exception exception = assertThrows(RecordNotFoundException.class, () -> {
            invoiceService.updateInvoice(invoiceId, updatedInvoice);
        });

        assertEquals("Invoice with ID '" + invoiceId + "'' not found", exception.getMessage());
        verify(invoiceRepo, times(1)).existsById(invoiceId);
        verify(invoiceRepo, times(0)).save(updatedInvoice);
    }

    @Test
    void testDeleteInvoice() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoiceRepo.save(invoice);
    
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceItemId(UUID.randomUUID());
        invoiceItem.setInvoice(invoice);
        invoiceItemService.saveInvoiceItem(invoiceItem);
    
        invoiceService.deleteInvoice(invoiceId, invoiceItemService);
    
        verify(invoiceRepo, times(1)).deleteById(invoiceId);
        verify(invoiceItemService, times(1)).deleteInvoiceItemsByInvoiceId(invoiceId);
    }

}
