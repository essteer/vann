package com.vann.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import com.vann.exceptions.*;
import com.vann.models.Customer;
import com.vann.repositories.CustomerRepo;


class CustomerServiceTest {

    @Mock
    private CustomerRepo customerRepo;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllCustomers() {
        Customer customer1 = new Customer("John Doe", "john.doe@example.com");
        Customer customer2 = new Customer("Jane Doe", "jane.doe@example.com");
    
        when(customerRepo.findAll()).thenReturn(List.of(customer1, customer2));
    
        List<Customer> customers = customerService.findAllCustomers();
    
        assertEquals(2, customers.size());
        assertTrue(customers.contains(customer1));
        assertTrue(customers.contains(customer2));
        verify(customerRepo, times(1)).findAll();
    }

    @Test
    void testFindCustomerById() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setName("John Doe");

        when(customerRepo.findById(id)).thenReturn(Optional.of(customer));
        Customer foundCustomer = customerService.findCustomerById(id);

        assertEquals("John Doe", foundCustomer.getName());
        verify(customerRepo, times(1)).findById(id);
    }

    @Test
    void testFindCustomerByIdNotFound() {
        UUID customerId = UUID.randomUUID();
        when(customerRepo.findById(customerId)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> customerService.findCustomerById(customerId));
    }

    @Test
    void testFindCustomerByEmail() {
        String email = "john.doe@example.com";
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setName("John Doe");
    
        when(customerRepo.findByEmail(email)).thenReturn(Optional.of(customer));
    
        Customer foundCustomer = customerService.findCustomerByEmail(email);
    
        assertEquals("John Doe", foundCustomer.getName());
        assertEquals(email, foundCustomer.getEmail());
        verify(customerRepo, times(1)).findByEmail(email);
    }

    @Test
    void testFindCustomerByEmailNotFound() {
        String email = "nonexistent@example.com";
        when(customerRepo.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> customerService.findCustomerByEmail(email));
    }

    @Test
    void testSaveCustomer() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");

        when(customerRepo.save(customer)).thenReturn(customer);

        Customer savedCustomer = customerService.saveCustomer(customer);

        assertNotNull(savedCustomer);
        assertEquals("John Doe", savedCustomer.getName());
        verify(customerRepo, times(1)).save(customer);
    }

    @Test
    void testUpdateCustomer() {
        UUID id = UUID.randomUUID();
        Customer existingCustomer = new Customer();
        existingCustomer.setName("John Doe");
        existingCustomer.setEmail("john.doe@example.com");
    
        Customer updatedCustomer = new Customer();
        updatedCustomer.setName("Jane Doe");
        updatedCustomer.setEmail("jane.doe@example.com");
    
        when(customerRepo.findById(id)).thenReturn(Optional.of(existingCustomer));
        when(customerRepo.save(existingCustomer)).thenReturn(updatedCustomer);
    
        Customer result = customerService.updateCustomer(id, updatedCustomer);
    
        assertEquals("Jane Doe", result.getName());
        assertEquals("jane.doe@example.com", result.getEmail());
        verify(customerRepo, times(1)).findById(id);
        verify(customerRepo, times(1)).save(existingCustomer);
    }

    @Test
    void testDeleteCustomer() {
        UUID id = UUID.randomUUID();

        when(customerRepo.existsById(id)).thenReturn(true);
        doNothing().when(customerRepo).deleteById(id);

        customerService.deleteCustomer(id);

        verify(customerRepo, times(1)).deleteById(id);
    }
}
