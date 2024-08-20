package com.vann;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.vann.repositories.CustomerRepo;
import com.vann.repositories.InvoiceRepo;
import com.vann.repositories.ProductRepo;

@Component
public class DataLoader implements ApplicationRunner {

    private CustomerRepo customerRepo;
    private InvoiceRepo invoiceRepo;
    private ProductRepo productRepo;
    

    public DataLoader(CustomerRepo customerRepo, InvoiceRepo invoiceRepo, ProductRepo productRepo) {
        super();
        this.customerRepo = customerRepo;
        this.invoiceRepo = invoiceRepo;
        this.productRepo = productRepo;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        
    }

}