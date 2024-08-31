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
import com.vann.utils.LogHandler;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        try {
            List<Invoice> invoices = invoiceService.findAllInvoices();
            if (invoices.isEmpty()) {
                LogHandler.status204NoContent("GET", InvoiceController.class, "getAllInvoices()");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            LogHandler.status200OK("GET", InvoiceController.class, "getAllInvoices()");
            return ResponseEntity.ok(invoices);
            
        } catch (Exception e) {
            LogHandler.status500InternalServerError("GET", InvoiceController.class, "getAllInvoices()", e.getMessage());
            throw e;
        }
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
    public ResponseEntity<?> getInvoicesByCustomerId(@PathVariable UUID id) {
        try {
            List<Invoice> invoices = invoiceService.findInvoicesByCustomerId(id);
            if (invoices.isEmpty()) {
                LogHandler.status204NoContent("GET", InvoiceController.class, "getInvoicesByCustomerId()");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            LogHandler.status200OK("GET", InvoiceController.class, "getInvoicesByCustomerId()");
            return ResponseEntity.ok(invoices);
        
        } catch (RecordNotFoundException e) {  // for customer ID not found
            LogHandler.status404NotFound("GET", InvoiceController.class, "getInvoicesByCustomerId()", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("GET", InvoiceController.class, "getInvoicesByCustomerId()", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/customer-email/{email}")
    public ResponseEntity<?> getInvoicesByCustomerEmail(@PathVariable String email) {
        try {
            List<Invoice> invoices = invoiceService.findInvoicesByCustomerEmail(email);
            if (invoices.isEmpty()) {
                LogHandler.status204NoContent("GET", InvoiceController.class, "getInvoicesByCustomerEmail()");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            LogHandler.status200OK("GET", InvoiceController.class, "getInvoicesByCustomerEmail()");
            return ResponseEntity.ok(invoices);

        } catch (RecordNotFoundException e) {  // for customer email not found
            LogHandler.status404NotFound("GET", InvoiceController.class, "getInvoicesByCustomerEmail()", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("GET", InvoiceController.class, "getInvoicesByCustomerEmail()", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoice(@PathVariable UUID id) {
        try {
            Invoice invoice = invoiceService.findInvoiceById(id);
            LogHandler.status200OK("GET", InvoiceController.class, "getInvoice()");
            return ResponseEntity.ok(invoice);
        
        } catch (RecordNotFoundException e) {
            LogHandler.status404NotFound("GET", InvoiceController.class, "getInvoice()", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("GET", InvoiceController.class, "getInvoice()", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable UUID id) {
        try {
            invoiceService.deleteInvoice(id);
            LogHandler.status204NoContent("DELETE", InvoiceController.class, "deleteInvoice()");
            return ResponseEntity.status(HttpStatus.OK).build();
        
        } catch (RecordNotFoundException e) {
            LogHandler.status404NotFound("DELETE", InvoiceController.class, "deleteInvoice()", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LogHandler.status500InternalServerError("DELETE", InvoiceController.class, "deleteInvoice()", e.getMessage());
            throw e;
        }
    }

}
