package app.ca.services;

import app.ca.services.model.Customer;
import app.ca.sotre.CustomerStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerStore customerStore;

    @Autowired
    public CustomerService(CustomerStore customerStore) {
        this.customerStore = customerStore;
    }

    @NonNull
    public Customer saveCustomer(@NonNull Customer customer) {
        return customerStore.saveCustomer(customer);
    }


    @Nullable
    public Customer getCustomer(@NonNull String customerId) {
        return customerStore.getCustomer(customerId);
    }

}
