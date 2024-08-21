package com.vann.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vann.model.enums.Colour;
import com.vann.model.enums.Size;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceProductTest {

    private InvoiceProduct invoiceProduct;
    private Invoice invoice;
    private Product product;

    @BeforeEach
    public void setUp() {
        invoice = new Invoice();
        product = new Product("Ring", "Gold ring", 499.99, "Available", "image.png", new Category("Jewellery", "Luxury items"), Size.US_07, Colour.YELLOW_GOLD);
        invoiceProduct = new InvoiceProduct(invoice, product, 2, 499.99);
    }

    @Test
    public void testInvoiceProductDefaultConstructor() {
        InvoiceProduct defaultInvoiceProduct = new InvoiceProduct();
        assertNotNull(defaultInvoiceProduct.getInvoiceProductUuid(), "UUID should be generated");
        assertNull(defaultInvoiceProduct.getInvoice(), "Invoice should be null");
        assertNull(defaultInvoiceProduct.getProduct(), "Product should be null");
        assertEquals(0, defaultInvoiceProduct.getQuantity(), "Quantity should be 0");
        assertEquals(0.0, defaultInvoiceProduct.getPrice(), "Price should be 0.0");
    }

    @Test
    public void testInvoiceProductParameterizedConstructor() {
        assertNotNull(invoiceProduct.getInvoiceProductUuid(), "UUID should be generated");
        assertEquals(invoice, invoiceProduct.getInvoice(), "Invoice should match");
        assertEquals(product, invoiceProduct.getProduct(), "Product should match");
        assertEquals(2, invoiceProduct.getQuantity(), "Quantity should match");
        assertEquals(499.99, invoiceProduct.getPrice(), "Price should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newUuid = UUID.randomUUID();
        invoiceProduct.setInvoiceProductUuid(newUuid);

        Invoice newInvoice = new Invoice(); // Alternatively, mock with Mockito
        Product newProduct = new Product("Necklace", "Silver necklace", 299.99, "Out of Stock", "necklace.png", new Category("Accessories", "Luxury items"), Size.LARGE, Colour.SILVER);

        invoiceProduct.setInvoice(newInvoice);
        invoiceProduct.setProduct(newProduct);
        invoiceProduct.setQuantity(3);
        invoiceProduct.setPrice(299.99);

        assertEquals(newUuid, invoiceProduct.getInvoiceProductUuid(), "UUID should match");
        assertEquals(newInvoice, invoiceProduct.getInvoice(), "Invoice should match");
        assertEquals(newProduct, invoiceProduct.getProduct(), "Product should match");
        assertEquals(3, invoiceProduct.getQuantity(), "Quantity should match");
        assertEquals(299.99, invoiceProduct.getPrice(), "Price should match");
    }

    @Test
    public void testToString() {
        String expectedString = "InvoiceProduct [invoiceProductUuid=" + invoiceProduct.getInvoiceProductUuid() +
                ", invoice=" + invoice.toString() + 
                ", product=" + product.toString() + 
                ", quantity=2, price=499.99]";

        assertEquals(expectedString, invoiceProduct.toString(), "toString should match the expected output");
    }
}
