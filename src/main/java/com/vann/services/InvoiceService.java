package com.vann.services;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.models.*;
import com.vann.repositories.InvoiceRepo;
import com.vann.utils.LogHandler;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;


@Service
public class InvoiceService {

    private final UserService userService;
    private final InvoiceItemService invoiceItemService;
    private final InvoiceRepo invoiceRepo;

    public InvoiceService(UserService userService, InvoiceItemService invoiceItemService, InvoiceRepo invoiceRepo) {
        this.userService = userService;
        this.invoiceItemService = invoiceItemService;
        this.invoiceRepo = invoiceRepo;
    }

    public Invoice createInvoice(User user, Map<UUID, Integer> cartItems, String billingAddress, String shippingAddress) {
        Invoice invoiceTemplate = createUserInvoice(user, billingAddress, shippingAddress);
        Invoice invoice = updateInvoice(invoiceTemplate, cartItems);

        return invoice;
    }

    public Invoice createUserInvoice(User potentialUser, String billingAddress, String shippingAddress) throws RecordNotFoundException {
        Invoice invoice = createBlankInvoice();
        User user = userService.findUserById(potentialUser.getId());
        invoice.setUser(user);
        invoice.setBillingAddress(billingAddress);
        invoice.setShippingAddress(shippingAddress);

        Invoice savedInvoice = saveInvoice(invoice);
        LogHandler.status201Created(InvoiceService.class + " | 1 record created | id=" + invoice.getId());

        return savedInvoice;
    }

    public Invoice createBlankInvoice() {
        Invoice invoice = new Invoice();
        return invoice;
    }

    public Invoice findInvoiceById(UUID id) throws RecordNotFoundException {
        Optional<Invoice> invoiceOptional = invoiceRepo.findById(id);

        if (invoiceOptional.isPresent()) {
            LogHandler.status200OK(InvoiceService.class + " | record found | id=" + id);
            return invoiceOptional.get();
        } else {
            throw new RecordNotFoundException(InvoiceService.class + " | record not found | id=" + id);
        }
    }

    public List<Invoice> findAllInvoices() {
        List<Invoice> invoices = invoiceRepo.findAll();
        logBulkFindOperation(invoices, "findAll()");
        return invoices;
    }

    private void logBulkFindOperation(List<Invoice> invoices, String details) {
        if (invoices.isEmpty()) {
            LogHandler.status204NoContent(InvoiceService.class + " | 0 records | " + details);
        } else {
            LogHandler.status200OK(InvoiceService.class + " | " + invoices.size() + " records | " + details);
        }
    }

    public List<Invoice> findInvoicesByUserId(UUID id) {
        List<Invoice> invoices = invoiceRepo.findByUser_Id(id);
        logBulkFindOperation(invoices, "findInvoicesByUser_Id()");
        return invoices;
    }

    public List<Invoice> findInvoicesByUserEmail(String email) {
        List<Invoice> invoices = invoiceRepo.findByUser_Email(email);
        logBulkFindOperation(invoices, "findByUser_Email()");
        return invoices;
    }

    public Invoice updateInvoice(Invoice invoice, Map<UUID, Integer> items) throws RecordNotFoundException {
        if ((invoiceRepo.existsById(invoice.getId()))) {
            List<InvoiceItem> invoiceTable = createInvoiceTable(items);
            invoice.setInvoiceItems(invoiceTable);
            invoice.calculateTotalAmount();

            Invoice updatedInvoice = saveInvoice(invoice);
            LogHandler.status200OK(InvoiceService.class + " | record updated | id=" + invoice.getId());
            
            return updatedInvoice;
        } else {
            throw new RecordNotFoundException(InvoiceService.class + " | record not found | id=" + invoice.getId());
        }
    }

    private List<InvoiceItem> createInvoiceTable(Map<UUID, Integer> items) {
        List<InvoiceItem> invoiceTable = new ArrayList<>();
        for (Entry<UUID, Integer> item : items.entrySet()) {

            UUID productId = item.getKey();
            Integer quantity = item.getValue();
            InvoiceItem invoiceItem = invoiceItemService.createInvoiceItem(productId, quantity);
            invoiceTable.add(invoiceItem);
        }
        return invoiceTable;
    }

    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepo.save(invoice);
    }

    public void deleteInvoice(UUID id) {
        Invoice invoice = findInvoiceById(id);
        List<InvoiceItem> invoiceItems = invoice.getInvoiceItems();
        for (InvoiceItem item : invoiceItems) {
            invoiceItemService.deleteInvoiceItem(item.getId());
        }
        invoiceRepo.deleteById(id);
        LogHandler.status204NoContent(InvoiceService.class + " | record deleted | id=" + id);
    }
}
