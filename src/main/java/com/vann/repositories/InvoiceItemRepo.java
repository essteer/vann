package com.vann.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vann.models.InvoiceItem;

public interface InvoiceItemRepo extends JpaRepository<InvoiceItem, UUID> {

}
