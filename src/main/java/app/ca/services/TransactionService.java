package app.ca.services;

import app.ca.services.model.Account;
import app.ca.sotre.AccountStore;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final AccountStore accountStore;
    private final DistributedLockService distributedLockService;

    @Autowired
    public TransactionService(AccountStore accountStore, DistributedLockService distributedLockService) {
        this.accountStore = accountStore;
        this.distributedLockService = distributedLockService;
    }

    @NonNull
    public Account credit(@NonNull Account account, @NonNull Money money) {
        if (!money.isPositiveOrZero()) {
            throw new IllegalArgumentException("invalid amount for credit: " + money);
        }
        if (money.getCurrency() != account.getCurrency()) {
            throw new IllegalArgumentException("invalid currency for credit: " + money);
        }
        var lock = distributedLockService.lock(account.getAccountId());
        try {
            lock.lock();
            Account latest = accountStore.getAccount(account.getAccountId());
            if (latest == null) {
                throw new IllegalArgumentException("unknown account");
            }
            Money newBalance = account.getBalance().add(money);
            Account updated = Account.builder().accountId(account.getAccountId()).customerId(account.getCustomerId()).balance(newBalance).build();
            return accountStore.saveAccount(updated);
        } finally {
            lock.unlock();
        }
    }

}
