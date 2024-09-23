package com.vann.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.vann.models.Invoice;
import com.vann.services.InvoiceService;


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

    @GetMapping("/customer-id/{id}")
    public ResponseEntity<List<Invoice>> getInvoicesByCustomerId(@PathVariable UUID id) {
        List<Invoice> invoices = invoiceService.findInvoicesByCustomerId(id);
        if (invoices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/customer-email/{email}")
    public ResponseEntity<List<Invoice>> getInvoicesByCustomerEmail(@PathVariable String email) {
        List<Invoice> invoices = invoiceService.findInvoicesByCustomerEmail(email);
        if (invoices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable UUID id) {
        Invoice invoice = invoiceService.findInvoiceById(id);
        return ResponseEntity.ok(invoice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteInvoice(@PathVariable UUID id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
