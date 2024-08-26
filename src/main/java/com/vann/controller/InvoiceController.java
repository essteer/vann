package com.vann.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.model.Invoice;
import com.vann.service.InvoiceService;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.findAllInvoices();
        if (invoices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/bw/{minAmount}/{maxAmount}")
    public ResponseEntity<List<Invoice>> getInvoicesByTotalAmountBetween(
        @PathVariable double minAmount, 
        @PathVariable double maxAmount
    ) {
        List<Invoice> invoices = invoiceService.findInvoicesByTotalAmountBetween(minAmount, maxAmount);
        if (invoices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/gt/{amount}")
    public ResponseEntity<List<Invoice>> getInvoicesByTotalAmountGreaterThan(@PathVariable double amount) {
        List<Invoice> invoices = invoiceService.findInvoicesByTotalAmountGreaterThan(amount);
        if (invoices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/lt/{amount}")
    public ResponseEntity<List<Invoice>> getInvoicesByTotalAmountLessThan(@PathVariable double amount) {
        List<Invoice> invoices = invoiceService.findInvoicesByTotalAmountLessThan(amount);
        if (invoices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/customer-id/{id}")
    public ResponseEntity<List<?>> getInvoicesByCustomerId(@PathVariable UUID id) {
        List<Invoice> invoices = invoiceService.findInvoicesByCustomerId(id);
        if (invoices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/customer-email/{email}")
    public ResponseEntity<List<?>> getInvoicesByCustomerEmail(@PathVariable String email) {
        List<Invoice> invoices = invoiceService.findInvoicesByCustomerEmail(email);
        if (invoices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoice(@PathVariable UUID id) {
        try {
            Invoice invoice = invoiceService.findInvoiceById(id);
            return ResponseEntity.ok(invoice);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable UUID id) {
        try {
            invoiceService.deleteInvoice(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
