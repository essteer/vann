package com.vann.service;

import com.vann.model.InvoiceItem;
import com.vann.repositories.InvoiceItemRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceItemService {

    private final InvoiceItemRepo invoiceItemRepo;

    public InvoiceItemService(InvoiceItemRepo invoiceItemRepo) {
        this.invoiceItemRepo = invoiceItemRepo;
    }

    public InvoiceItem saveInvoiceItem(InvoiceItem invoiceItem) {
        return invoiceItemRepo.save(invoiceItem);
    }

    public Optional<InvoiceItem> findInvoiceItemById(UUID invoiceItemUuid) {
        return invoiceItemRepo.findById(invoiceItemUuid);
    }

    public InvoiceItem updateInvoiceItem(UUID invoiceItemUuid, InvoiceItem updatedInvoiceItem) {
        // Check if the InvoiceItem exists
        if (!invoiceItemRepo.existsById(invoiceItemUuid)) {
            throw new IllegalArgumentException("InvoiceItem not found");
        }
        // Set the ID of the updated InvoiceItem
        updatedInvoiceItem.setInvoiceItemUuid(invoiceItemUuid);
        // Save the updated InvoiceItem
        return invoiceItemRepo.save(updatedInvoiceItem);
    }

    public void deleteInvoiceItem(UUID invoiceItemUuid) {
        invoiceItemRepo.deleteById(invoiceItemUuid);
    }

    public void deleteInvoiceItemsByInvoiceId(UUID invoiceId) {
        for (InvoiceItem invoiceItem : invoiceItemRepo.findAll()) {
            if (invoiceItem.getInvoice().getInvoiceId() == invoiceId) {
                deleteInvoiceItem(invoiceItem.getInvoiceItemUuid());
            }
        }
    }
}
