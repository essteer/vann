package com.vann.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vann.model.Invoice;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, UUID> {

}
