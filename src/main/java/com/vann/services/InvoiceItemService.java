package com.vann.services;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.models.*;
import com.vann.repositories.InvoiceItemRepo;
import com.vann.utils.LogHandler;

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

    public InvoiceItem createInvoiceItem(UUID productId, int quantity) throws IllegalArgumentException, RecordNotFoundException {
        if (isValidQuantity(quantity)) {

            Product product = productService.findProductById(productId);
            String productDetails = product.toString();
            double productUnitPrice = product.getPrice();
            
            InvoiceItem invoiceItem = new InvoiceItem(productId, quantity);
            invoiceItem.setUnitPrice(productUnitPrice);
            invoiceItem.setProductDetails(productDetails);
            invoiceItem.setProductDetails(productDetails);
            
            InvoiceItem savedInvoiceItem = saveInvoiceItem(invoiceItem);
            LogHandler.status200OK(InvoiceItemService.class + " | record created | id=" + invoiceItem.getId());
            return savedInvoiceItem;
        
        } else {
            throw new IllegalArgumentException(InvoiceItemService.class + " | invalid quantity | must be greater than 0: " + quantity);
        }
    }

    private boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }

    public Optional<InvoiceItem> findInvoiceItemById(UUID id) throws RecordNotFoundException {
        Optional<InvoiceItem> invoiceItemOptional = invoiceItemRepo.findById(id);

        if (invoiceItemOptional.isPresent()) {
            LogHandler.status200OK(InvoiceItemService.class + " | record found | id=" + id);
            return invoiceItemOptional;
        } else {
            throw new RecordNotFoundException(InvoiceItemService.class + " | record not found | id=" + id);
        }
    }

    public InvoiceItem updateInvoiceItem(UUID id, InvoiceItem updatedInvoiceItem) throws RecordNotFoundException {
        if (!invoiceItemRepo.existsById(id)) {
            throw new RecordNotFoundException(InvoiceItemService.class + " | record not found | id=" + id);
        }
        return saveInvoiceItem(updatedInvoiceItem);
    }

    public InvoiceItem saveInvoiceItem(InvoiceItem invoiceItem) {
        return invoiceItemRepo.save(invoiceItem);
    }

    public void deleteInvoiceItem(UUID id) {
        invoiceItemRepo.deleteById(id);
        LogHandler.status204NoContent(InvoiceItemService.class + " | record deleted | id=" + id);
    }

}
