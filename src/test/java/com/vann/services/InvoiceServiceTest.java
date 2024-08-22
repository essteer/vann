package com.vann.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    void testDeleteInvoice() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoiceRepo.save(invoice);
    
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceItemUuid(UUID.randomUUID());
        invoiceItem.setInvoice(invoice);
        invoiceItemService.saveInvoiceItem(invoiceItem);
    
        invoiceService.deleteInvoice(invoiceId, invoiceItemService);
    
        verify(invoiceRepo, times(1)).deleteById(invoiceId);
        verify(invoiceItemService, times(1)).deleteInvoiceItemsByInvoiceId(invoiceId);
    }

}
