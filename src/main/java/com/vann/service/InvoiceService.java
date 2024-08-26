package com.vann.service;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.model.Customer;
import com.vann.model.Invoice;
import com.vann.model.InvoiceItem;
import com.vann.repositories.InvoiceRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceService {

    private final CustomerService customerService;
    private final InvoiceItemService invoiceItemService;
    private final InvoiceRepo invoiceRepo;

    public InvoiceService(CustomerService customerService, InvoiceItemService invoiceItemService, InvoiceRepo invoiceRepo) {
        this.customerService = customerService;
        this.invoiceItemService = invoiceItemService;
        this.invoiceRepo = invoiceRepo;
    }

    public Invoice createBlankInvoice() {
        Invoice invoice = new Invoice();
        return invoice;
    }

    public Invoice createCustomerInvoice(UUID customerId, String billAddress, String shipAddress) throws RecordNotFoundException {
        Customer customer = customerService.findCustomerById(customerId);
        Invoice invoice = createBlankInvoice();
        invoice.setInvoiceCustomerId(customerId);
        invoice.setInvoiceCustomerName(customer.getCustomerName());
        invoice.setInvoiceCustomerEmail(customer.getCustomerEmail());
        invoice.setInvoiceBillAddress(billAddress);
        invoice.setInvoiceShipAddress(shipAddress);
        return saveInvoice(invoice);
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
        return invoiceRepo.findByInvoiceCustomerId(customerId);
    }

    public List<Invoice> findInvoicesByCustomerEmail(String email) {
        return invoiceRepo.findByInvoiceCustomerEmail(email);
    }

    public Invoice findInvoiceById(UUID invoiceId) throws RecordNotFoundException {
        Optional<Invoice> invoiceOptional = invoiceRepo.findById(invoiceId);
        return invoiceOptional.orElseThrow(() -> 
            new RecordNotFoundException("Invoice with ID '" + invoiceId + "'' not found"));
    }

    public Invoice updateInvoice(UUID invoiceId, Map<UUID, Integer> items) throws RecordNotFoundException {
        if (!invoiceRepo.existsById(invoiceId)) {
            throw new RecordNotFoundException("Invoice with ID '" + invoiceId + "'' not found");
        }
        Invoice invoice = findInvoiceById(invoiceId);

        List<InvoiceItem> invoiceTable = createInvoiceTable(items);
        invoice.setInvoiceItems(invoiceTable);

        invoice.calculateTotalAmount();
        return saveInvoice(invoice);
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
        invoice.generateIdIfAbsent();
        return invoiceRepo.save(invoice);
    }

    public void deleteInvoice(UUID invoiceId) {
        Invoice invoice = findInvoiceById(invoiceId);
        List<InvoiceItem> invoiceItems = invoice.getInvoiceItems();
        for (InvoiceItem item : invoiceItems) {
            invoiceItemService.deleteInvoiceItem(item.getInvoiceItemId());
        }
        invoiceRepo.deleteById(invoiceId);
    }
}
