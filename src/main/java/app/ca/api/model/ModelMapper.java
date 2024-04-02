package app.ca.api.model;

import app.ca.api.model.AccountDto;
import app.ca.api.model.CustomerDto;
import app.ca.services.model.Account;
import app.ca.services.model.Customer;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * Helper class to convert Api model to Service model
 */
@Service
public class ModelMapper {

    @NonNull
    public CustomerDto customerToDto(@NonNull Customer customer) {
        return CustomerDto.builder().customerId(customer.getCustomerId()).name(customer.getName()).surname(customer.getSurname()).build();
    }

    @NonNull
    public AccountDto accountToDto(@NonNull Account account) {
        return AccountDto.builder().accountId(account.getAccountId()).balance(account.getBalance()).build();
    }
}
