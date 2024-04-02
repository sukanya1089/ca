package app.ca.sotre;

import app.ca.services.model.Customer;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CustomerStore {

    private final Map<String, Customer> customersById = new ConcurrentHashMap<>();

    @NonNull
    public Customer saveCustomer(@NonNull Customer customer) {
        customersById.put(customer.getCustomerId(), customer);
        return customer;
    }

    @Nullable
    public Customer getCustomer(@NonNull String customerId) {
        return customersById.get(customerId);
    }
}
