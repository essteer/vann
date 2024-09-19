package com.vann.services;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.models.*;
import com.vann.repositories.InvoiceItemRepo;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InvoiceItemService {

    private final InvoiceItemRepo invoiceItemRepo;
    private final ProductService productService;

    public InvoiceItemService(InvoiceItemRepo invoiceItemRepo, ProductService productService) {
        this.invoiceItemRepo = invoiceItemRepo;
        this.productService = productService;
    }

    public InvoiceItem createInvoiceItem(UUID productId, int quantity) throws RecordNotFoundException {
        Product product = productService.findProductById(productId);
        String productDetails = product.toString();
        double productUnitPrice = product.getProductPrice();

        InvoiceItem invoiceItem = new InvoiceItem(productId, quantity);
        invoiceItem.setUnitPrice(productUnitPrice);
        invoiceItem.setProductDetails(productDetails);
        invoiceItem.setProductDetails(productDetails);
        
        saveInvoiceItem(invoiceItem);
        return invoiceItem;
    }

    public Optional<InvoiceItem> findInvoiceItemById(UUID invoiceItemUuid) {
        return invoiceItemRepo.findById(invoiceItemUuid);
    }

    public InvoiceItem updateInvoiceItem(UUID invoiceItemUuid, InvoiceItem updatedInvoiceItem) throws RecordNotFoundException {
        if (!invoiceItemRepo.existsById(invoiceItemUuid)) {
            throw new RecordNotFoundException("InvoiceItem with ID '" + invoiceItemUuid + "' not found");
        }
        updatedInvoiceItem.setInvoiceItemId(invoiceItemUuid);
        return saveInvoiceItem(updatedInvoiceItem);
    }

    public InvoiceItem saveInvoiceItem(InvoiceItem invoiceItem) {
        return invoiceItemRepo.save(invoiceItem);
    }

    public void deleteInvoiceItem(UUID invoiceItemId) {
            invoiceItemRepo.deleteById(invoiceItemId);
    }

}
