package app.ca.services;

import app.ca.services.model.Account;
import app.ca.sotre.AccountStore;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import static org.mockito.Mockito.*;

class TransactionServiceTest {

    AccountStore accountStore;
    DistributedLockService distributedLockService;
    TransactionService transactionService;
    final String accountId = UUID.randomUUID().toString();
    final String customerId = UUID.randomUUID().toString();

    @BeforeEach
    void setupTransactionService() {
        accountStore = mock(AccountStore.class);
        distributedLockService = mock(DistributedLockService.class);
        transactionService = new TransactionService(accountStore, distributedLockService);
    }

    @Test
    @DisplayName("credit function should reject invalid amount")
    void testCreditForInvalidAmount() {
        Account account = Account.builder().accountId(accountId).customerId(customerId).balance(Money.zero(Monetary.getCurrency("EUR"))).build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionService.credit(account, Money.of(-1, account.getCurrency()));
        });
    }

    @Test
    @DisplayName("credit function should reject invalid currency")
    void testCreditForInvalidCurrency() {
        Account account = Account.builder().accountId(accountId).customerId(customerId).balance(Money.zero(Monetary.getCurrency("EUR"))).build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionService.credit(account, Money.of(10, Monetary.getCurrency("USD")));
        });
    }

    @Test
    @DisplayName("credit function should reject non existing account")
    void testCreditForNonExistingAccount() {
        when(accountStore.getAccount(accountId)).thenReturn(null);
        when(distributedLockService.lock(accountId)).thenReturn(new ReentrantLock(true));
        Account account = Account.builder().accountId(accountId).customerId(customerId).balance(Money.zero(Monetary.getCurrency("EUR"))).build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionService.credit(account, Money.of(10, Monetary.getCurrency("EUR")));
        });
    }

    @Test
    @DisplayName("credit function should save account to store with updated balance")
    void testCreditForBalance() {
        Account accountWithZeroBalance = Account.builder().accountId(accountId).customerId(customerId).balance(Money.zero(Monetary.getCurrency("EUR"))).build();
        Account accountWith10EurBalance = Account.builder().accountId(accountId).customerId(customerId).balance(Money.of(10, Monetary.getCurrency("EUR"))).build();
        when(accountStore.getAccount(accountId)).thenReturn(accountWithZeroBalance);
        when (accountStore.saveAccount(accountWith10EurBalance)).thenReturn(accountWith10EurBalance);
        when(distributedLockService.lock(accountId)).thenReturn(new ReentrantLock(true));
        var updatedAccount = transactionService.credit(accountWithZeroBalance, Money.of(10, Monetary.getCurrency("EUR")));
        Assertions.assertEquals(accountWith10EurBalance, updatedAccount);
    }

}