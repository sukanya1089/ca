package app.ca.api;

import app.ca.api.model.AccountDto;
import app.ca.api.model.AccountOpeningRequest;
import app.ca.api.model.ModelMapper;
import app.ca.services.AccountService;
import app.ca.services.CustomerService;
import app.ca.services.TransactionService;
import app.ca.services.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsController {

    private final CustomerService customerService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final ModelMapper modelMapper;

    @Autowired
    public AccountsController(CustomerService customerService, AccountService accountService, TransactionService transactionService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/accounts")
    public ResponseEntity<AccountDto> newAccount(@RequestBody AccountOpeningRequest accountOpeningRequest) {
        if (customerService.getCustomer(accountOpeningRequest.getCustomerId()) == null) {
            return ResponseEntity.badRequest().build();
        }
        if (accountOpeningRequest.getInitialAmount().isNegative()) {
            return ResponseEntity.badRequest().build();
        }
        Account account = accountService.openAccount(accountOpeningRequest.getCustomerId(), accountOpeningRequest.getInitialAmount().getCurrency());
        account = transactionService.credit(account, accountOpeningRequest.getInitialAmount());
        return ResponseEntity.ok(modelMapper.accountToDto(account));
    }


}
