package com.vann.service;

import com.vann.exceptions.InvoiceNotFoundException;
import com.vann.model.Invoice;
import com.vann.repositories.InvoiceRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceService {

    private final InvoiceRepo invoiceRepo;

    public InvoiceService(InvoiceRepo invoiceRepo) {
        this.invoiceRepo = invoiceRepo;
    }

    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepo.save(invoice);
    }

    public List<Invoice> findAllInvoices() {
        return invoiceRepo.findAll();
    }

    public Invoice findInvoiceById(UUID invoiceId) {
        Optional<Invoice> invoiceOptional = invoiceRepo.findById(invoiceId);
        return invoiceOptional.orElseThrow(() -> 
            new InvoiceNotFoundException("Invoice with ID " + invoiceId + " not found"));
    }

    public Invoice updateInvoice(UUID invoiceId, Invoice updatedInvoice) {
        // Check if the Invoice exists
        if (!invoiceRepo.existsById(invoiceId)) {
            throw new InvoiceNotFoundException("Invoice with ID " + invoiceId + " not found");
        }
        // Set the ID of the updated Invoice
        updatedInvoice.setInvoiceId(invoiceId);
        // Save the updated Invoice
        return invoiceRepo.save(updatedInvoice);
    }

    public void deleteInvoice(UUID invoiceId, InvoiceItemService invoiceItemService) {
        invoiceRepo.deleteById(invoiceId);
        invoiceItemService.deleteInvoiceItemsByInvoiceId(invoiceId);
    }
}
