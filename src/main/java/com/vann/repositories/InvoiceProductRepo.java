package com.vann.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vann.model.InvoiceProduct;

public interface InvoiceProductRepo extends JpaRepository<InvoiceProduct, UUID> {

}
