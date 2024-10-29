package com.vann.repositories;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vann.models.Invoice;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, UUID> {

    List<Invoice> findByUser_Email(String email);
    List<Invoice> findByUser_Id(UUID id);

}
