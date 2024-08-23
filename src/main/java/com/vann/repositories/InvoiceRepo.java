package com.vann.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vann.model.Invoice;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, UUID> {

    List<Invoice> findByInvoiceCustomer_CustomerEmail(String customerEmail);
    List<Invoice> findByInvoiceCustomer_Id(UUID id);

    List<Invoice> findByInvoiceTotalAmountBetween(double minAmount, double maxAmount);
    List<Invoice> findByInvoiceTotalAmountGreaterThan(double amount);
    List<Invoice> findByInvoiceTotalAmountLessThan(double amount);

}
