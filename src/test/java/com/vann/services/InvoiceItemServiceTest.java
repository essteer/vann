package com.vann.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.model.Category;
import com.vann.model.InvoiceItem;
import com.vann.model.Product;
import com.vann.model.enums.Colour;
import com.vann.model.enums.Size;
import com.vann.repositories.InvoiceItemRepo;
import com.vann.service.InvoiceItemService;
import com.vann.service.ProductService;

public class InvoiceItemServiceTest {

    @Mock
    private InvoiceItemRepo invoiceItemRepo;

    @Mock
    private ProductService productService;

    @InjectMocks
    private InvoiceItemService invoiceItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateInvoiceItem() throws RecordNotFoundException {
        UUID productId = UUID.randomUUID();
        int quantity = 5;
        Product product = new Product("Product Name", 10.0, "image.jpg", new Category(), Size.SMALL, Colour.BLACK);
        product.setProductId(productId);
        
        when(productService.findProductById(productId)).thenReturn(product);
        when(invoiceItemRepo.save(any(InvoiceItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        InvoiceItem invoiceItem = invoiceItemService.createInvoiceItem(productId, quantity);
    
        assertNotNull(invoiceItem);
        assertEquals(productId, invoiceItem.getInvoiceItemProductId());
        assertEquals(quantity, invoiceItem.getQuantity());
        assertEquals(product.getProductPrice(), invoiceItem.getUnitPrice());
        assertEquals(product.toString(), invoiceItem.getProductDetails());
        verify(productService, times(1)).findProductById(productId);
        verify(invoiceItemRepo, times(1)).save(any(InvoiceItem.class));
    }

    @Test
    void testFindInvoiceItemById_Success() {
        UUID invoiceItemId = UUID.randomUUID();
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceItemId(invoiceItemId);
        
        when(invoiceItemRepo.findById(invoiceItemId)).thenReturn(Optional.of(invoiceItem));
        
        Optional<InvoiceItem> foundInvoiceItem = invoiceItemService.findInvoiceItemById(invoiceItemId);
        
        assertTrue(foundInvoiceItem.isPresent());
        assertEquals(invoiceItemId, foundInvoiceItem.get().getInvoiceItemId());
        verify(invoiceItemRepo, times(1)).findById(invoiceItemId);
    }

    @Test
    void testFindInvoiceItemById_NotFound() {
        UUID invoiceItemId = UUID.randomUUID();
        
        when(invoiceItemRepo.findById(invoiceItemId)).thenReturn(Optional.empty());
        
        Optional<InvoiceItem> foundInvoiceItem = invoiceItemService.findInvoiceItemById(invoiceItemId);
        
        assertFalse(foundInvoiceItem.isPresent());
        verify(invoiceItemRepo, times(1)).findById(invoiceItemId);
    }

    @Test
    void testUpdateInvoiceItem_Success() throws RecordNotFoundException {
        UUID invoiceItemId = UUID.randomUUID();
        InvoiceItem existingInvoiceItem = new InvoiceItem();
        existingInvoiceItem.setInvoiceItemId(invoiceItemId);
        
        InvoiceItem updatedInvoiceItem = new InvoiceItem(UUID.randomUUID(), 10);
        
        when(invoiceItemRepo.existsById(invoiceItemId)).thenReturn(true);
        when(invoiceItemRepo.save(any(InvoiceItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        InvoiceItem result = invoiceItemService.updateInvoiceItem(invoiceItemId, updatedInvoiceItem);
        
        assertNotNull(result);
        assertEquals(invoiceItemId, result.getInvoiceItemId());
        assertEquals(updatedInvoiceItem.getQuantity(), result.getQuantity());
        verify(invoiceItemRepo, times(1)).existsById(invoiceItemId);
        verify(invoiceItemRepo, times(1)).save(updatedInvoiceItem);
    }

    @Test
    void testUpdateInvoiceItem_NotFound() {
        UUID invoiceItemId = UUID.randomUUID();
        InvoiceItem updatedInvoiceItem = new InvoiceItem(UUID.randomUUID(), 10);
        
        when(invoiceItemRepo.existsById(invoiceItemId)).thenReturn(false);
        
        assertThrows(RecordNotFoundException.class, () -> {
            invoiceItemService.updateInvoiceItem(invoiceItemId, updatedInvoiceItem);
        });

        verify(invoiceItemRepo, times(1)).existsById(invoiceItemId);
        verify(invoiceItemRepo, times(0)).save(any(InvoiceItem.class));
    }

    @Test
    void testDeleteInvoiceItem() {
        UUID invoiceItemId = UUID.randomUUID();
        
        doNothing().when(invoiceItemRepo).deleteById(invoiceItemId);
        
        invoiceItemService.deleteInvoiceItem(invoiceItemId);
        
        verify(invoiceItemRepo, times(1)).deleteById(invoiceItemId);
    }
}
