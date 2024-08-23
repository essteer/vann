package com.vann.service;

import com.vann.exceptions.RecordNotFoundException;
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

    public List<Invoice> findInvoicesByTotalAmountBetween(double minAmount, double maxAmount) {
        return invoiceRepo.findByInvoiceTotalAmountBetween(minAmount, maxAmount);
    }

    public List<Invoice> findInvoicesByTotalAmountGreaterThan(double amount) {
        return invoiceRepo.findByInvoiceTotalAmountGreaterThan(amount);
    }

    public List<Invoice> findInvoicesByTotalAmountLessThan(double amount) {
        return invoiceRepo.findByInvoiceTotalAmountLessThan(amount);
    }

    public List<Invoice> findInvoicesByCustomerId(UUID customerId) {
        return invoiceRepo.findByInvoiceCustomer_Id(customerId);
    }

    public List<Invoice> findInvoicesByCustomerEmail(String email) {
        return invoiceRepo.findByInvoiceCustomer_CustomerEmail(email);
    }

    public Invoice findInvoiceById(UUID invoiceId) {
        Optional<Invoice> invoiceOptional = invoiceRepo.findById(invoiceId);
        return invoiceOptional.orElseThrow(() -> 
            new RecordNotFoundException("Invoice with ID '" + invoiceId + "'' not found"));
    }

    public Invoice updateInvoice(UUID invoiceId, Invoice updatedInvoice) {
        if (!invoiceRepo.existsById(invoiceId)) {
            throw new RecordNotFoundException("Invoice with ID '" + invoiceId + "'' not found");
        }
        updatedInvoice.setInvoiceId(invoiceId);
        return invoiceRepo.save(updatedInvoice);
    }

    public void deleteInvoice(UUID invoiceId, InvoiceItemService invoiceItemService) {
        invoiceRepo.deleteById(invoiceId);
        invoiceItemService.deleteInvoiceItemsByInvoiceId(invoiceId);
    }
}
