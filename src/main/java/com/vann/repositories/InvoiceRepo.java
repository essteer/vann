package com.vann.repositories;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vann.models.Invoice;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, UUID> {

    List<Invoice> findByInvoiceCustomerEmail(String customerEmail);
    List<Invoice> findByInvoiceCustomerId(UUID id);

    List<Invoice> findByInvoiceTotalAmountBetween(double minAmount, double maxAmount);
    List<Invoice> findByInvoiceTotalAmountGreaterThan(double amount);
    List<Invoice> findByInvoiceTotalAmountLessThan(double amount);

}
