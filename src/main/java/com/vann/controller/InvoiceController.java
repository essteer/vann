package com.vann.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/bw/{minAmount}/{maxAmount}")
    public ResponseEntity<List<Invoice>> getInvoicesByTotalAmountBetween(
        @PathVariable double minAmount, 
        @PathVariable double maxAmount
    ) {
        List<Invoice> invoices = invoiceService.findInvoicesByTotalAmountBetween(minAmount, maxAmount);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/gt/{amount}")
    public ResponseEntity<List<Invoice>> getInvoicesByTotalAmountGreaterThan(@PathVariable double amount) {
        List<Invoice> invoices = invoiceService.findInvoicesByTotalAmountGreaterThan(amount);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/lt/{amount}")
    public ResponseEntity<List<Invoice>> getInvoicesByTotalAmountLessThan(@PathVariable double amount) {
        List<Invoice> invoices = invoiceService.findInvoicesByTotalAmountLessThan(amount);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<List<?>> getInvoicesByCustomerId(@PathVariable UUID id) {
        List<Invoice> invoices = invoiceService.findInvoicesByCustomerId(id);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/invoice/{email}")
    public ResponseEntity<List<?>> getInvoicesByCustomerEmail(@PathVariable String email) {
        List<Invoice> invoices = invoiceService.findInvoicesByCustomerEmail(email);
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

    @PostMapping
    public ResponseEntity<Invoice> saveInvoice(@RequestBody Invoice invoice) {
        Invoice savedInvoice = invoiceService.saveInvoice(invoice);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedInvoice.getInvoiceId())
            .toUri();
        return ResponseEntity.created(location).body(savedInvoice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInvoice(@PathVariable UUID id, @RequestBody Invoice updatedInvoice) {
        try {
            Invoice updated = invoiceService.updateInvoice(id, updatedInvoice);
            return ResponseEntity.ok(updated);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
