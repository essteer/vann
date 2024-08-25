package com.vann;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.vann.repositories.*;
import com.vann.service.*;

@Component
public class DataLoader implements ApplicationRunner {

    private CartRepo cartRepo;
    private CategoryRepo categoryRepo;
    private CustomerRepo customerRepo;
    private InvoiceItemRepo invoiceItemRepo;
    private InvoiceRepo invoiceRepo;
    private ProductRepo productRepo;

    public DataLoader(CartRepo cartRepo, CategoryRepo categoryRepo, CustomerRepo customerRepo, InvoiceItemRepo invoiceItemRepo, InvoiceRepo invoiceRepo, ProductRepo productRepo) {
        super();
        this.cartRepo = cartRepo;
        this.categoryRepo = categoryRepo;
        this.customerRepo = customerRepo;
        this.invoiceItemRepo = invoiceItemRepo;
        this.invoiceRepo = invoiceRepo;
        this.productRepo = productRepo;
    }

    @Override
    public void run(ApplicationArguments args) {

        @SuppressWarnings("unused")
        CategoryService categoryService = new CategoryService(this.categoryRepo);
        ProductService productService = new ProductService(this.productRepo);
        CustomerService customerService = new CustomerService(this.customerRepo);
        @SuppressWarnings("unused")
        CartService cartService = new CartService(this.cartRepo, customerService, productService);
        @SuppressWarnings("unused")
        InvoiceItemService invoiceItemService = new InvoiceItemService(this.invoiceItemRepo);
        @SuppressWarnings("unused")
        InvoiceService invoiceService = new InvoiceService(this.invoiceRepo);
        
    }

}