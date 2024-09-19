package com.vann.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import com.vann.exceptions.RecordNotFoundException;
import com.vann.models.*;
import com.vann.repositories.InvoiceRepo;


public class InvoiceServiceTest {

    @Mock
    private InvoiceRepo invoiceRepo;

    @Mock
    private InvoiceItemService invoiceItemService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private InvoiceService invoiceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test 
    void testCreateBlankInvoice() {
        Invoice invoice = invoiceService.createBlankInvoice();
        assertNotNull(invoice);
    }

    @Test
    void testCreateCustomerInvoice() throws RecordNotFoundException {
        UUID customerId = UUID.randomUUID();
        String billAddress = "12 Oak Street";
        String shipAddress = "15 Elm Street";
    
        Customer customer = new Customer("John Doe", "john@example.com");
        customer.setCustomerId(customerId);
        when(customerService.findCustomerById(customerId)).thenReturn(customer);
        
        Invoice invoice = new Invoice();
        invoice.setInvoiceCustomerId(customerId);
        invoice.setInvoiceCustomerName("John Doe");
        invoice.setInvoiceCustomerEmail("john@example.com");
        invoice.setInvoiceBillAddress(billAddress);
        invoice.setInvoiceShipAddress(shipAddress);
        
        when(invoiceRepo.save(any(Invoice.class))).thenReturn(invoice);
        
        Invoice result = invoiceService.createCustomerInvoice(customerId, billAddress, shipAddress);
    
        assertNotNull(result);
        assertEquals(customerId, result.getInvoiceCustomerId());
        assertEquals("John Doe", result.getInvoiceCustomerName());
        assertEquals("john@example.com", result.getInvoiceCustomerEmail());
        assertEquals(billAddress, result.getInvoiceBillAddress());
        assertEquals(shipAddress, result.getInvoiceShipAddress());
        verify(invoiceRepo, times(1)).save(any(Invoice.class));
    }


    @Test
    void testFindAllInvoices() {
        Invoice invoice1 = new Invoice();
        Invoice invoice2 = new Invoice();

        when(invoiceRepo.findAll()).thenReturn(Arrays.asList(invoice1, invoice2));

        List<Invoice> invoices = invoiceService.findAllInvoices();

        assertEquals(2, invoices.size());
        assertTrue(invoices.contains(invoice1));
        assertTrue(invoices.contains(invoice2));
        verify(invoiceRepo, times(1)).findAll();
    }

    @Test
    void testFindInvoicesByTotalAmountBetween() {
        double minAmount = 100.0;
        double maxAmount = 500.0;

        Invoice invoice1 = new Invoice();
        Invoice invoice2 = new Invoice();

        when(invoiceRepo.findByInvoiceTotalAmountBetween(minAmount, maxAmount))
            .thenReturn(Arrays.asList(invoice1, invoice2));

        List<Invoice> invoices = invoiceService.findInvoicesByTotalAmountBetween(minAmount, maxAmount);

        assertEquals(2, invoices.size());
        assertTrue(invoices.contains(invoice1));
        assertTrue(invoices.contains(invoice2));
        verify(invoiceRepo, times(1)).findByInvoiceTotalAmountBetween(minAmount, maxAmount);
    }

    @Test
    void testFindInvoicesByTotalAmountGreaterThan() {
        double amount = 150.0;

        Invoice invoice1 = new Invoice();
        Invoice invoice2 = new Invoice();

        when(invoiceRepo.findByInvoiceTotalAmountGreaterThan(amount))
            .thenReturn(Arrays.asList(invoice1, invoice2));

        List<Invoice> invoices = invoiceService.findInvoicesByTotalAmountGreaterThan(amount);

        assertEquals(2, invoices.size());
        assertTrue(invoices.contains(invoice1));
        assertTrue(invoices.contains(invoice2));
        verify(invoiceRepo, times(1)).findByInvoiceTotalAmountGreaterThan(amount);
    }

    @Test
    void testFindInvoicesByTotalAmountLessThan() {
        double amount = 250.0;

        Invoice invoice1 = new Invoice();
        Invoice invoice2 = new Invoice();

        when(invoiceRepo.findByInvoiceTotalAmountLessThan(amount))
            .thenReturn(Arrays.asList(invoice1, invoice2));

        List<Invoice> invoices = invoiceService.findInvoicesByTotalAmountLessThan(amount);

        assertEquals(2, invoices.size());
        assertTrue(invoices.contains(invoice1));
        assertTrue(invoices.contains(invoice2));
        verify(invoiceRepo, times(1)).findByInvoiceTotalAmountLessThan(amount);
    }

    @Test
    void testFindInvoicesByCustomerId() {
        UUID customerId = UUID.randomUUID();
        Invoice invoice1 = new Invoice();
        Invoice invoice2 = new Invoice();

        when(invoiceRepo.findByInvoiceCustomerId(customerId)).thenReturn(Arrays.asList(invoice1, invoice2));

        List<Invoice> invoices = invoiceService.findInvoicesByCustomerId(customerId);

        assertEquals(2, invoices.size());
        assertTrue(invoices.contains(invoice1));
        assertTrue(invoices.contains(invoice2));
        verify(invoiceRepo, times(1)).findByInvoiceCustomerId(customerId);
    }

    @Test
    void testFindInvoicesByCustomerEmail() {
        String email = "customer@example.com";
        Invoice invoice1 = new Invoice();
        Invoice invoice2 = new Invoice();

        when(invoiceRepo.findByInvoiceCustomerEmail(email)).thenReturn(Arrays.asList(invoice1, invoice2));

        List<Invoice> invoices = invoiceService.findInvoicesByCustomerEmail(email);

        assertEquals(2, invoices.size());
        assertTrue(invoices.contains(invoice1));
        assertTrue(invoices.contains(invoice2));
        verify(invoiceRepo, times(1)).findByInvoiceCustomerEmail(email);
    }

    @Test
    void testFindInvoiceById() throws RecordNotFoundException {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);

        when(invoiceRepo.findById(invoiceId)).thenReturn(Optional.of(invoice));

        Invoice foundInvoice = invoiceService.findInvoiceById(invoiceId);
        assertNotNull(foundInvoice);
        assertEquals(invoiceId, foundInvoice.getInvoiceId());
        verify(invoiceRepo, times(1)).findById(invoiceId);
    }

    @Test
    void testFindInvoiceById_NotFound() {
        UUID invoiceId = UUID.randomUUID();

        when(invoiceRepo.findById(invoiceId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecordNotFoundException.class, () -> {
            invoiceService.findInvoiceById(invoiceId);
        });

        assertEquals("Invoice with ID '" + invoiceId + "'' not found", exception.getMessage());
        verify(invoiceRepo, times(1)).findById(invoiceId);
    }

    @Test
    void testUpdateInvoice_Success() throws RecordNotFoundException {
        UUID invoiceId = UUID.randomUUID();
        Invoice existingInvoice = new Invoice();
        existingInvoice.setInvoiceId(invoiceId);
    
        Map<UUID, Integer> items = new HashMap<>();
        items.put(UUID.randomUUID(), 2);
        items.put(UUID.randomUUID(), 3);
    
        List<InvoiceItem> invoiceTable = new ArrayList<>();
        for (Map.Entry<UUID, Integer> entry : items.entrySet()) {
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setInvoiceItemProductId(entry.getKey());
            invoiceItem.setQuantity(entry.getValue());
            invoiceTable.add(invoiceItem);
        }
    
        when(invoiceRepo.existsById(invoiceId)).thenReturn(true);
        when(invoiceRepo.findById(invoiceId)).thenReturn(Optional.of(existingInvoice));
        when(invoiceItemService.createInvoiceItem(any(UUID.class), anyInt())).thenAnswer(invocation -> {
            UUID productId = invocation.getArgument(0);
            Integer quantity = invocation.getArgument(1);
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setInvoiceItemProductId(productId);
            invoiceItem.setQuantity(quantity);
            return invoiceItem;
        });
        when(invoiceRepo.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice invoice = invocation.getArgument(0);
            return invoice;
        });
    
        Invoice updatedInvoice = invoiceService.updateInvoice(invoiceId, items);
    
        assertNotNull(updatedInvoice);
        assertEquals(invoiceTable.size(), updatedInvoice.getInvoiceItems().size());
        for (int i = 0; i < invoiceTable.size(); i++) {
            assertEquals(invoiceTable.get(i).getInvoiceItemProductId(), updatedInvoice.getInvoiceItems().get(i).getInvoiceItemProductId());
            assertEquals(invoiceTable.get(i).getQuantity(), updatedInvoice.getInvoiceItems().get(i).getQuantity());
        }
        verify(invoiceRepo, times(1)).existsById(invoiceId);
        verify(invoiceRepo, times(1)).findById(invoiceId);
        verify(invoiceItemService, times(invoiceTable.size())).createInvoiceItem(any(UUID.class), anyInt());
        verify(invoiceRepo, times(1)).save(updatedInvoice);
    }
    


    @Test
    void testUpdateInvoice_NotFound() {
        UUID invoiceId = UUID.randomUUID();
        Map<UUID, Integer> items = new HashMap<>();
        items.put(UUID.randomUUID(), 2);

        when(invoiceRepo.existsById(invoiceId)).thenReturn(false);

        Exception exception = assertThrows(RecordNotFoundException.class, () -> {
            invoiceService.updateInvoice(invoiceId, items);
        });

        assertEquals("Invoice with ID '" + invoiceId + "'' not found", exception.getMessage());
        verify(invoiceRepo, times(1)).existsById(invoiceId);
        verify(invoiceRepo, times(0)).save(any());
    }

    @Test
    void testDeleteInvoice() throws RecordNotFoundException {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceId);
        invoice.setInvoiceItems(new ArrayList<>());
        
        when(invoiceRepo.findById(invoiceId)).thenReturn(Optional.of(invoice));

        invoiceService.deleteInvoice(invoiceId);

        verify(invoiceRepo, times(1)).deleteById(invoiceId);
    }
}
