package app.ca.services;

import app.ca.services.model.Account;
import app.ca.sotre.AccountStore;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.money.CurrencyUnit;
import java.util.List;

/**
 * Service to manage accounts like:
 * <ol>
 * <li>Opening Account</li>
 * <li>Closing Account</li>
 * </ol>
 * <b>Note:</b> Only opening account is implemented as asked.
 */
@Service
public class AccountService {

    private final AccountIdService accountIdService;
    private final AccountStore accountStore;

    @Autowired
    public AccountService(AccountIdService accountIdService, AccountStore accountStore) {
        this.accountIdService = accountIdService;
        this.accountStore = accountStore;
    }

    @NonNull
    public Account openAccount(@NonNull String customerId, @NonNull CurrencyUnit currency) {
        String accountId = accountIdService.nextAccountId();
        Account account = Account.builder().accountId(accountId).customerId(customerId).balance(Money.zero(currency)).build();
        return accountStore.saveAccount(account);
    }


    @NonNull
    public List<Account> getAccountsForCustomer(@NonNull String customerId) {
        return accountStore.getAccountsForCustomer(customerId);
    }

}
