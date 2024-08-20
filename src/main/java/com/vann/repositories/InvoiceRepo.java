package com.vann.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vann.model.Invoice;

public interface InvoiceRepo extends JpaRepository<Invoice, UUID> {

}
