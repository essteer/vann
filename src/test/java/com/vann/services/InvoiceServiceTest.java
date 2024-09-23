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
        when(customerService.findCustomerById(customerId)).thenReturn(customer);
        
        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setBillingAddress(billAddress);
        invoice.setShippingAddress(shipAddress);
        
        when(invoiceRepo.save(any(Invoice.class))).thenReturn(invoice);
        
        Invoice result = invoiceService.createCustomerInvoice(customer, billAddress, shipAddress);
    
        assertNotNull(result);
        assertEquals(customer, result.getCustomer());
        assertEquals("John Doe", result.getCustomer().getName());
        assertEquals("john@example.com", result.getCustomer().getEmail());
        assertEquals(billAddress, result.getBillingAddress());
        assertEquals(shipAddress, result.getShippingAddress());
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
    void testFindInvoicesByCustomerId() {
        UUID customerId = UUID.randomUUID();
        Invoice invoice1 = new Invoice();
        Invoice invoice2 = new Invoice();

        when(invoiceRepo.findByCustomer_Id(customerId)).thenReturn(Arrays.asList(invoice1, invoice2));

        List<Invoice> invoices = invoiceService.findInvoicesByCustomerId(customerId);

        assertEquals(2, invoices.size());
        assertTrue(invoices.contains(invoice1));
        assertTrue(invoices.contains(invoice2));
        verify(invoiceRepo, times(1)).findByCustomer_Id(customerId);
    }

    @Test
    void testFindInvoicesByCustomerEmail() {
        String email = "customer@example.com";
        Invoice invoice1 = new Invoice();
        Invoice invoice2 = new Invoice();

        when(invoiceRepo.findByCustomer_Email(email)).thenReturn(Arrays.asList(invoice1, invoice2));

        List<Invoice> invoices = invoiceService.findInvoicesByCustomerEmail(email);

        assertEquals(2, invoices.size());
        assertTrue(invoices.contains(invoice1));
        assertTrue(invoices.contains(invoice2));
        verify(invoiceRepo, times(1)).findByCustomer_Email(email);
    }

    @Test
    void testFindInvoiceById() throws RecordNotFoundException {
        UUID id = UUID.randomUUID();
        Invoice invoice = new Invoice();

        when(invoiceRepo.findById(id)).thenReturn(Optional.of(invoice));

        Invoice foundInvoice = invoiceService.findInvoiceById(id);
        assertNotNull(foundInvoice);
        verify(invoiceRepo, times(1)).findById(id);
    }

    @Test
    void testFindInvoiceById_NotFound() {
        UUID invoiceId = UUID.randomUUID();

        when(invoiceRepo.findById(invoiceId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecordNotFoundException.class, () -> {
            invoiceService.findInvoiceById(invoiceId);
        });

        assertEquals(InvoiceService.class + " | record not found | id=" + invoiceId, exception.getMessage());
        verify(invoiceRepo, times(1)).findById(invoiceId);
    }

    @Test
    void testUpdateInvoice_Success() throws RecordNotFoundException {
        UUID id = UUID.randomUUID();
        Invoice existingInvoice = new Invoice();
        existingInvoice.setInvoiceItems(new ArrayList<>()); // Initialize invoiceItems list
        
        Map<UUID, Integer> items = new HashMap<>();
        items.put(UUID.randomUUID(), 2);
        items.put(UUID.randomUUID(), 3);
        
        List<InvoiceItem> invoiceTable = new ArrayList<>();
        for (Map.Entry<UUID, Integer> entry : items.entrySet()) {
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setProductId(entry.getKey());
            invoiceItem.setQuantity(entry.getValue());
            invoiceTable.add(invoiceItem);
        }
        
        existingInvoice.setInvoiceItems(invoiceTable); // Set invoiceItems list on existingInvoice
        
        when(invoiceRepo.existsById(id)).thenReturn(true);
        when(invoiceRepo.findById(id)).thenReturn(Optional.of(existingInvoice));
        when(invoiceItemService.createInvoiceItem(any(UUID.class), anyInt())).thenAnswer(invocation -> {
            UUID productId = invocation.getArgument(0);
            Integer quantity = invocation.getArgument(1);
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setProductId(productId);
            invoiceItem.setQuantity(quantity);
            return invoiceItem;
        });
        when(invoiceRepo.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice invoice = invocation.getArgument(0);
            return invoice;
        });
        when(invoiceRepo.existsById(existingInvoice.getId())).thenReturn(true);
        Invoice updatedInvoice = invoiceService.updateInvoice(existingInvoice, items);
        
        assertNotNull(updatedInvoice);
        assertEquals(invoiceTable.size(), updatedInvoice.getInvoiceItems().size());
        for (int i = 0; i < invoiceTable.size(); i++) {
            assertEquals(invoiceTable.get(i).getProductId(), updatedInvoice.getInvoiceItems().get(i).getProductId());
            assertEquals(invoiceTable.get(i).getQuantity(), updatedInvoice.getInvoiceItems().get(i).getQuantity());
        }
        verify(invoiceItemService, times(invoiceTable.size())).createInvoiceItem(any(UUID.class), anyInt());
        verify(invoiceRepo, times(1)).save(updatedInvoice);
    }

    @Test
    void testUpdateInvoice_NotFound() {
        Invoice nonexistingInvoice = new Invoice();
        Map<UUID, Integer> items = new HashMap<>();
        items.put(UUID.randomUUID(), 2);

        when(invoiceRepo.existsById(nonexistingInvoice.getId())).thenReturn(false);

        Exception exception = assertThrows(RecordNotFoundException.class, () -> {
            invoiceService.updateInvoice(nonexistingInvoice, items);
        });

        assertEquals(InvoiceService.class + " | record not found | id=" + null, exception.getMessage());
        verify(invoiceRepo, times(1)).existsById(nonexistingInvoice.getId());
        verify(invoiceRepo, times(0)).save(any());
    }

    @Test
    void testDeleteInvoice() throws RecordNotFoundException {
        UUID id = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setInvoiceItems(new ArrayList<>());
        
        when(invoiceRepo.findById(id)).thenReturn(Optional.of(invoice));

        invoiceService.deleteInvoice(id);

        verify(invoiceRepo, times(1)).deleteById(id);
    }
}
