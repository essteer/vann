package com.vann.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vann.model.InvoiceItem;

public interface InvoiceItemRepo extends JpaRepository<InvoiceItem, UUID> {

}
