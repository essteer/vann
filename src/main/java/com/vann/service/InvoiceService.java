package com.vann.service;

import com.vann.model.Invoice;
import com.vann.model.InvoiceItem;
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

    public Optional<Invoice> findInvoiceById(UUID invoiceUuid) {
        return invoiceRepo.findById(invoiceUuid);
    }

    public Invoice updateInvoice(UUID invoiceUuid, Invoice updatedInvoice) {
        // Check if the Invoice exists
        if (!invoiceRepo.existsById(invoiceUuid)) {
            throw new IllegalArgumentException("Invoice not found");
        }
        // Set the ID of the updated Invoice
        updatedInvoice.setInvoiceUuid(invoiceUuid);
        // Save the updated Invoice
        return invoiceRepo.save(updatedInvoice);
    }

    public void deleteInvoice(UUID invoiceUuid, InvoiceItemService invoiceItemService) {
        Optional<Invoice> invoiceOpt = invoiceRepo.findById(invoiceUuid);
        if (invoiceOpt.isPresent()) {
            Invoice invoice = invoiceOpt.get();
            for (InvoiceItem invoiceItem : invoice.getInvoiceItems()) {
                invoiceItemService.deleteInvoiceItem(invoiceItem.getInvoiceItemUuid());
            }
            invoiceRepo.deleteById(invoiceUuid);
        }
    }
}
