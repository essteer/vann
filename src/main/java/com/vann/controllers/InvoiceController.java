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

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Invoice>> getInvoicesByUserId(@PathVariable UUID id) {
        List<Invoice> invoices = invoiceService.findInvoicesByUserId(id);
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
