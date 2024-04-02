package app.ca.sotre;

import app.ca.services.model.Account;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountStore {

    private final Map<String, Account> accountsById = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> accountsIdsByCustomerId = new ConcurrentHashMap<>();


    @NonNull
    public Account saveAccount(@NonNull Account account) {
        synchronized (accountsById) {
            accountsById.put(account.getAccountId(), account);
            accountsIdsByCustomerId.compute(account.getCustomerId(), (customerId, existingAccountIds) -> {
                if (existingAccountIds == null) {
                    return Set.of(account.getAccountId());
                }
                Set<String> customerAccountIds = new HashSet<>(existingAccountIds);
                customerAccountIds.add(account.getAccountId());
                return customerAccountIds;
            });
        }
        return account;
    }

    @Nullable
    public Account getAccount(@NonNull String accountId) {
        return accountsById.get(accountId);
    }


    @NonNull
    public List<Account> getAccountsForCustomer(@NonNull String customerId) {
        Set<String> accountIds = accountsIdsByCustomerId.getOrDefault(customerId, Set.of());
        return accountIds.stream().map(this::getAccount).filter(Objects::nonNull).toList();
    }
}
