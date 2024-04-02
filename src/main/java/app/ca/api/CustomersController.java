package app.ca.api;

import app.ca.api.model.CustomerCreationRequest;
import app.ca.api.model.CustomerWithAccountsDto;
import app.ca.api.model.ModelMapper;
import app.ca.services.AccountService;
import app.ca.services.CustomerService;
import app.ca.services.model.Account;
import app.ca.services.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CustomersController {

    private final CustomerService customerService;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomersController(CustomerService customerService, AccountService accountService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.accountService = accountService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<CustomerWithAccountsDto> getCustomer(@PathVariable("customerId") String customerId) {
        Customer customer = customerService.getCustomer(customerId);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        List<Account> accounts = accountService.getAccountsForCustomer(customer.getCustomerId());
        CustomerWithAccountsDto response = CustomerWithAccountsDto.builder()
                .customer(modelMapper.customerToDto(customer))
                .accounts(accounts.stream().map(modelMapper::accountToDto).toList())
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/customers")
    public ResponseEntity<CustomerWithAccountsDto> createCustomer(@RequestBody CustomerCreationRequest customerCreationRequest) {
        Customer customer = Customer.builder()
                .customerId(UUID.randomUUID().toString())
                .name(customerCreationRequest.getName())
                .surname(customerCreationRequest.getSurname()).build();
        customer = customerService.saveCustomer(customer);
        CustomerWithAccountsDto response = CustomerWithAccountsDto.builder()
                .customer(modelMapper.customerToDto(customer))
                .accounts(List.of())
                .build();
        return ResponseEntity.ok(response);
    }

}
