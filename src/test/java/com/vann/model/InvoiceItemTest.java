package com.vann.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceItemTest {

    private InvoiceItem item;

    @BeforeEach
    public void setUp() {
        item = new InvoiceItem(UUID.randomUUID(), 10);
    }

    @Test
    public void testInvoiceItemDefaultConstructor() {
        InvoiceItem defaultInvoiceItem = new InvoiceItem();
        defaultInvoiceItem.generateIdIfAbsent();
        assertNotNull(defaultInvoiceItem.getInvoiceItemId(), "UUID should be generated");
        assertNull(defaultInvoiceItem.getInvoiceItemProductId(), "productId should be null");
        assertEquals(0, defaultInvoiceItem.getQuantity(), "quantity should be 0");
        assertEquals(0.0, defaultInvoiceItem.getUnitPrice(), "unitPrice should be 0.0");
        assertNull(defaultInvoiceItem.getProductDetails(), "productDetails should be null");
        assertEquals(0.0, defaultInvoiceItem.calculateInvoiceItemSubtotal(), "subtotal should be 0.0");
        assertEquals("InvoiceItem [id=" + defaultInvoiceItem.getInvoiceItemId() + ", unitprice=0.0, quantity=0, subtotal=0.0, Product=[null]]", defaultInvoiceItem.toString());
    }

    @Test
    public void testInvoiceItemParameterizedConstructor() {
        UUID productId = UUID.randomUUID();
        InvoiceItem parameterizedInvoiceItem = new InvoiceItem(productId, 3);
        assertNotNull(parameterizedInvoiceItem.getInvoiceItemId(), "UUID should be generated");
        assertEquals(productId, parameterizedInvoiceItem.getInvoiceItemProductId(), "productId should match");
        assertEquals(3, parameterizedInvoiceItem.getQuantity(), "quantity should match");
        assertEquals(0.0, parameterizedInvoiceItem.getUnitPrice(), "unitPrice should be 0.0");
        assertNull(parameterizedInvoiceItem.getProductDetails(), "productDetails should be null");
        assertEquals(0.0, parameterizedInvoiceItem.calculateInvoiceItemSubtotal(), "subtotal should be 0.0");
        assertNull(item.getProductDetails(), "Product details should be null");
        assertEquals("InvoiceItem [id=" + parameterizedInvoiceItem.getInvoiceItemId() + ", unitprice=0.0, quantity=3, subtotal=0.0, Product=[null]]", parameterizedInvoiceItem.toString());
    }

    @Test
    public void testSettersAndGetters() {
        UUID newUuid = UUID.randomUUID();
        int quantity = 20;
        double unitPrice = 30.0;
        item.setInvoiceItemProductId(newUuid);
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);
        item.setProductDetails("Product Details");

        assertEquals(newUuid, item.getInvoiceItemProductId(), "Product ID should match");
        assertEquals(20, item.getQuantity(), "Quantity should match");
        assertEquals(30.0, item.getUnitPrice(), "Unit price should match");
        assertEquals("Product Details", item.getProductDetails(), "Product details should match");
    }

    @Test
    public void testCalculateInvoiceItemSubtotal() {
        item.setQuantity(10);
        item.setUnitPrice(20.0);
        assertEquals(200.0, item.calculateInvoiceItemSubtotal(), "Subtotal should match");
    }

    @Test
    public void testToString() {
        String expectedString = "InvoiceItem [id=" + item.getInvoiceItemId() +
                ", unitprice=" + item.getUnitPrice() + ", quantity=" + item.getQuantity() + ", subtotal=" + item.calculateInvoiceItemSubtotal() + ", Product=[" + item.getProductDetails() + "]]";

        assertEquals(expectedString, item.toString(), "toString should match the expected output");
    }
}