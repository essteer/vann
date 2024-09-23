package com.vann.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.models.*;
import com.vann.models.enums.*;
import com.vann.repositories.InvoiceItemRepo;


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
        Product product = new Product(new Category(), "Product Name", 10.0, "image.jpg", Size.SMALL, Colour.BLACK);
        
        when(productService.findProductById(productId)).thenReturn(product);
        when(invoiceItemRepo.save(any(InvoiceItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        InvoiceItem invoiceItem = invoiceItemService.createInvoiceItem(productId, quantity);
    
        assertNotNull(invoiceItem);
        assertEquals(productId, invoiceItem.getProductId());
        assertEquals(quantity, invoiceItem.getQuantity());
        assertEquals(product.getPrice(), invoiceItem.getUnitPrice());
        assertEquals(product.toString(), invoiceItem.getProductDetails());
        verify(productService, times(1)).findProductById(productId);
        verify(invoiceItemRepo, times(1)).save(any(InvoiceItem.class));
    }

    @Test
    void testFindInvoiceItemById_Success() {
        UUID invoiceItemId = UUID.randomUUID();
        InvoiceItem invoiceItem = new InvoiceItem();
        
        when(invoiceItemRepo.findById(invoiceItemId)).thenReturn(Optional.of(invoiceItem));
        Optional<InvoiceItem> foundInvoiceItem = invoiceItemService.findInvoiceItemById(invoiceItemId);
        
        assertTrue(foundInvoiceItem.isPresent());
        verify(invoiceItemRepo, times(1)).findById(invoiceItemId);
    }

    @Test
    void testFindInvoiceItemById_NotFound() {
        UUID id = UUID.randomUUID();
        
        when(invoiceItemRepo.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecordNotFoundException.class, () -> {
            invoiceItemService.findInvoiceItemById(id);
        });

        assertEquals(InvoiceItemService.class + " | record not found | id=" + id, exception.getMessage());
        verify(invoiceItemRepo, times(1)).findById(id);
    }

    @Test
    void testUpdateInvoiceItem_Success() throws RecordNotFoundException {
        UUID invoiceItemId = UUID.randomUUID();
        InvoiceItem updatedInvoiceItem = new InvoiceItem(UUID.randomUUID(), 10);
        
        when(invoiceItemRepo.existsById(invoiceItemId)).thenReturn(true);
        when(invoiceItemRepo.save(any(InvoiceItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        InvoiceItem result = invoiceItemService.updateInvoiceItem(invoiceItemId, updatedInvoiceItem);
        
        assertNotNull(result);
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
