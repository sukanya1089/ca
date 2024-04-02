package app.ca.store;

import app.ca.services.model.Account;
import app.ca.sotre.AccountStore;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;
import java.util.List;
import java.util.UUID;

class AccountStoreTest {

    AccountStore accountStore;

    @BeforeEach
    void setupAccountStore() {
        accountStore = new AccountStore();
    }

    @Test
    @DisplayName("saveAccount should persist new account to underlying store")
    void testSaveAccountForNewAccount() {
        String accountId = UUID.randomUUID().toString();
        String customerId = UUID.randomUUID().toString();
        Assertions.assertNull(accountStore.getAccount(accountId));
        Account account = Account.builder().accountId(accountId).customerId(customerId).balance(Money.zero(Monetary.getCurrency("EUR"))).build();
        var savedAccount = accountStore.saveAccount(account);
        Assertions.assertEquals(account, savedAccount);
    }

    @Test
    @DisplayName("saveAccount should persist existing account to underlying store")
    void testSaveAccountForExistingAccount() {
        String accountId = UUID.randomUUID().toString();
        String customerId = UUID.randomUUID().toString();
        Account account = Account.builder().accountId(accountId).customerId(customerId).balance(Money.zero(Monetary.getCurrency("EUR"))).build();
        accountStore.saveAccount(account);
        // Update
        Account updatedAccount = Account.builder().accountId(accountId).customerId(customerId).balance(Money.of(100, Monetary.getCurrency("EUR"))).build();
        var savedAccount = accountStore.saveAccount(updatedAccount);
        Assertions.assertEquals(updatedAccount, savedAccount);
    }


    @Test
    @DisplayName("getAccount should return null if account does not exists")
    void testGetAccountForNonExistingAccount() {
        String accountId = UUID.randomUUID().toString();
        Assertions.assertNull(accountStore.getAccount(accountId));
    }

    @Test
    @DisplayName("getAccount should return account when exists")
    void testGetAccountForExistingAccount() {
        String accountId = UUID.randomUUID().toString();
        String customerId = UUID.randomUUID().toString();
        Account account = Account.builder().accountId(accountId).customerId(customerId).balance(Money.zero(Monetary.getCurrency("EUR"))).build();
        accountStore.saveAccount(account);
        Assertions.assertEquals(account, accountStore.getAccount(accountId));
    }

    @Test
    @DisplayName("getAccountsForCustomer should return empty list when there are no accounts")
    void testGetAccountsForCustomerForNewCustomer() {
        String customerId = UUID.randomUUID().toString();
        Assertions.assertTrue(accountStore.getAccountsForCustomer(customerId).isEmpty());
    }

    @Test
    @DisplayName("getAccountsForCustomer should return all accounts belonging to customer")
    void testGetAccountsForCustomerForExistingCustomer() {
        String accountId1 = UUID.randomUUID().toString();
        String customerId = UUID.randomUUID().toString();
        Account account1 = Account.builder().accountId(accountId1).customerId(customerId).balance(Money.zero(Monetary.getCurrency("EUR"))).build();
        accountStore.saveAccount(account1);
        Assertions.assertEquals(1, accountStore.getAccountsForCustomer(customerId).size());
        Assertions.assertEquals(account1, accountStore.getAccountsForCustomer(customerId).get(0));

        String accountId2 = UUID.randomUUID().toString();
        Account account2 = Account.builder().accountId(accountId2).customerId(customerId).balance(Money.of(10, Monetary.getCurrency("EUR"))).build();
        accountStore.saveAccount(account2);
        Assertions.assertEquals(2, accountStore.getAccountsForCustomer(customerId).size());
        Assertions.assertTrue(accountStore.getAccountsForCustomer(customerId).containsAll(List.of(account1, account2)));
    }

}