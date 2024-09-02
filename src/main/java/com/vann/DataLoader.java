package com.vann;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.vann.repositories.*;
import com.vann.service.*;
import com.vann.utils.LogHandler;

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
    public void run(ApplicationArguments args) throws RuntimeException {

        try {
            CategoryService categoryService = new CategoryService(this.categoryRepo);
            LogHandler.serviceClassInitOK(categoryService.getClass().getName());
            
            ProductService productService = new ProductService(this.productRepo, categoryService);
            LogHandler.serviceClassInitOK(productService.getClass().getName());
            
            CustomerService customerService = new CustomerService(this.customerRepo);
            LogHandler.serviceClassInitOK(customerService.getClass().getName());
            
            InvoiceItemService invoiceItemService = new InvoiceItemService(this.invoiceItemRepo, productService);
            LogHandler.serviceClassInitOK(invoiceItemService.getClass().getName());
            
            InvoiceService invoiceService = new InvoiceService(customerService, invoiceItemService, this.invoiceRepo);
            LogHandler.serviceClassInitOK(invoiceService.getClass().getName());
            
            CartService cartService = new CartService(this.cartRepo, customerService, invoiceService, productService);
            LogHandler.serviceClassInitOK(cartService.getClass().getName());
        
        } catch (Exception e) {
            LogHandler.serviceClassInitError(e.getMessage());
            throw new RuntimeException();
        }
    }
}