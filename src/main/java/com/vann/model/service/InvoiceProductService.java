package com.vann.model.service;

import com.vann.model.InvoiceProduct;
import com.vann.repositories.InvoiceProductRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceProductService {

    private final InvoiceProductRepo invoiceProductRepo;

    public InvoiceProductService(InvoiceProductRepo invoiceProductRepo) {
        this.invoiceProductRepo = invoiceProductRepo;
    }

    public InvoiceProduct saveInvoiceProduct(InvoiceProduct invoiceProduct) {
        return invoiceProductRepo.save(invoiceProduct);
    }

    public Optional<InvoiceProduct> findInvoiceProductById(UUID invoiceProductUuid) {
        return invoiceProductRepo.findById(invoiceProductUuid);
    }

    public InvoiceProduct updateInvoiceProduct(UUID invoiceProductUuid, InvoiceProduct updatedInvoiceProduct) {
        // Check if the InvoiceProduct exists
        if (!invoiceProductRepo.existsById(invoiceProductUuid)) {
            throw new IllegalArgumentException("InvoiceProduct not found");
        }
        // Set the ID of the updated InvoiceProduct
        updatedInvoiceProduct.setInvoiceProductUuid(invoiceProductUuid);
        // Save the updated InvoiceProduct
        return invoiceProductRepo.save(updatedInvoiceProduct);
    }

    public void deleteInvoiceProduct(UUID invoiceProductUuid) {
        invoiceProductRepo.deleteById(invoiceProductUuid);
    }
}
