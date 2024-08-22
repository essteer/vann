package com.vann.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vann.model.enums.CategoryType;
import com.vann.model.enums.Colour;
import com.vann.model.enums.Size;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceItemTest {

    private InvoiceItem invoiceItem;
    private Invoice invoice;
    private Product product;

    @BeforeEach
    public void setUp() {
        invoice = new Invoice();
        product = new Product("Ring", 499.99, "image.png", new Category(CategoryType.RING, "Jewellery"), Size.US_07, Colour.YELLOW_GOLD);
        invoiceItem = new InvoiceItem(invoice, product, 2, 499.99);
    }

    @Test
    public void testInvoiceItemDefaultConstructor() {
        InvoiceItem defaultInvoiceItem = new InvoiceItem();
        defaultInvoiceItem.generateId();
        assertNotNull(defaultInvoiceItem.getInvoiceItemId(), "UUID should be generated");
        assertNull(defaultInvoiceItem.getInvoice(), "Invoice should be null");
        assertNull(defaultInvoiceItem.getInvoiceItem(), "Product should be null");
        assertEquals(0, defaultInvoiceItem.getQuantity(), "Quantity should be 0");
        assertEquals(0.0, defaultInvoiceItem.getPrice(), "Price should be 0.0");
    }

    @Test
    public void testInvoiceItemParameterizedConstructor() {
        assertNotNull(invoiceItem.getInvoiceItemId(), "UUID should be generated");
        assertEquals(invoice, invoiceItem.getInvoice(), "Invoice should match");
        assertEquals(product, invoiceItem.getInvoiceItem(), "Product should match");
        assertEquals(2, invoiceItem.getQuantity(), "Quantity should match");
        assertEquals(499.99, invoiceItem.getPrice(), "Price should match");
    }

    @Test
    public void testSettersAndGetters() {
        UUID newId = UUID.randomUUID();
        invoiceItem.setInvoiceItemId(newId);

        Invoice newInvoice = new Invoice(); // Alternatively, mock with Mockito
        Product newProduct = new Product("Necklace", 299.99, "necklace.png", new Category(CategoryType.NECKLACE, "Accessories"), Size.LARGE, Colour.SILVER);

        invoiceItem.setInvoice(newInvoice);
        invoiceItem.setInvoiceItem(newProduct);
        invoiceItem.setQuantity(3);
        invoiceItem.setPrice(299.99);

        assertEquals(newId, invoiceItem.getInvoiceItemId(), "UUID should match");
        assertEquals(newInvoice, invoiceItem.getInvoice(), "Invoice should match");
        assertEquals(newProduct, invoiceItem.getInvoiceItem(), "Product should match");
        assertEquals(3, invoiceItem.getQuantity(), "Quantity should match");
        assertEquals(299.99, invoiceItem.getPrice(), "Price should match");
    }

    @Test
    public void testToString() {
        String expectedString = "InvoiceItem [id=" + invoiceItem.getInvoiceItemId() +
                ", invoice=" + invoice.toString() + 
                ", invoiceItem=" + product.toString() + 
                ", quantity=2, price=499.99]";

        assertEquals(expectedString, invoiceItem.toString(), "toString should match the expected output");
    }
}
