package app.ca.services;

import app.ca.services.model.Customer;
import app.ca.sotre.CustomerStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Test
    @DisplayName("saveCustomer should delegate it to store")
    void testSaveCustomer() {
        Customer customer = Customer.builder().customerId(UUID.randomUUID().toString()).name("name").surname("sn").build();
        CustomerStore customerStore = mock(CustomerStore.class);
        when(customerStore.saveCustomer(customer)).thenReturn(customer);

        CustomerService customerService = new CustomerService(customerStore);
        Assertions.assertEquals(customer, customerService.saveCustomer(customer));

        verify(customerStore, times(1)).saveCustomer(customer);
    }

    @Test
    @DisplayName("getCustomer should delegate it to store")
    void testGetCustomer() {
        Customer customer = Customer.builder().customerId(UUID.randomUUID().toString()).name("name").surname("sn").build();
        CustomerStore customerStore = mock(CustomerStore.class);
        when(customerStore.getCustomer(customer.getCustomerId())).thenReturn(customer);

        CustomerService customerService = new CustomerService(customerStore);
        Assertions.assertEquals(customer, customerService.getCustomer(customer.getCustomerId()));

        verify(customerStore, times(1)).getCustomer(customer.getCustomerId());
    }
}