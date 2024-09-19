package com.vann.repositories;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vann.models.Invoice;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, UUID> {

    List<Invoice> findByCustomerEmail(String customerEmail);
    List<Invoice> findByCustomerId(UUID id);

    List<Invoice> findByTotalAmountBetween(double minAmount, double maxAmount);
    List<Invoice> findByTotalAmountGreaterThan(double amount);
    List<Invoice> findByTotalAmountLessThan(double amount);

}
