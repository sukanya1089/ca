package app.ca.store;

import app.ca.services.model.Customer;
import app.ca.sotre.CustomerStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class CustomerStoreTest {

    CustomerStore customerStore;

    @BeforeEach
    void setupCustomerStore() {
        customerStore = new CustomerStore();
    }

    @Test
    @DisplayName("saveCustomer should persist new customer to underlying store")
    void testSaveCustomerForNewCustomer() {
        String customerId = UUID.randomUUID().toString();
        Assertions.assertNull(customerStore.getCustomer(customerId));
        Customer customer = Customer.builder().customerId(customerId).name("name").surname("sn").build();
        var savedCustomer = customerStore.saveCustomer(customer);
        Assertions.assertEquals(customer, savedCustomer);
    }

    @Test
    @DisplayName("saveCustomer should persist existing customer to underlying store")
    void testSaveCustomerForExistingCustomer() {
        String customerId = UUID.randomUUID().toString();
        Customer customer = Customer.builder().customerId(customerId).name("name").surname("sn").build();
        customerStore.saveCustomer(customer);
        // Update
        Customer updatedCustomer = Customer.builder().customerId(customerId).name("name2").surname("sn2").build();
        var savedCustomer = customerStore.saveCustomer(updatedCustomer);
        Assertions.assertEquals(updatedCustomer, savedCustomer);
    }


    @Test
    @DisplayName("getCustomer should return null if customer does not exists")
    void testGetCustomerForNonExistingCustomer() {
        String customerId = UUID.randomUUID().toString();
        Assertions.assertNull(customerStore.getCustomer(customerId));
    }

    @Test
    @DisplayName("getCustomer should return customer when exists")
    void testGetCustomerForExistingCustomer() {
        String customerId = UUID.randomUUID().toString();
        Customer customer = Customer.builder().customerId(customerId).name("name").surname("sn").build();
        customerStore.saveCustomer(customer);
        Assertions.assertEquals(customer, customerStore.getCustomer(customerId));
    }

}