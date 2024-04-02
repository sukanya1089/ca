package app.ca.services;

import app.ca.services.model.Account;
import app.ca.sotre.AccountStore;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Test
    @DisplayName("openAccount should create account in underlying store with the account received from AccountIdService")
    void testOpenAccount() {
        String accountId = UUID.randomUUID().toString();
        String customerId = UUID.randomUUID().toString();
        Account account = Account.builder().accountId(accountId).customerId(customerId).balance(Money.zero(Monetary.getCurrency("EUR"))).build();

        AccountIdService accountIdService = mock(AccountIdService.class);
        AccountStore accountStore = mock(AccountStore.class);
        when(accountIdService.nextAccountId()).thenReturn(accountId);
        when(accountStore.saveAccount(eq(account))).thenReturn(account);

        AccountService accountService = new AccountService(accountIdService, accountStore);
        Assertions.assertEquals(account, accountService.openAccount(customerId, Monetary.getCurrency("EUR")));

        verify(accountIdService, times(1)).nextAccountId();
        verify(accountStore, times(1)).saveAccount(account);
    }

    @Test
    @DisplayName("getAccountsForCustomer should return customer accounts by delegating it to AccountStore")
    void testGetAccountsForCustomer() {
        String accountId = UUID.randomUUID().toString();
        String customerId = UUID.randomUUID().toString();
        Account account = Account.builder().accountId(accountId).customerId(customerId).balance(Money.zero(Monetary.getCurrency("EUR"))).build();

        AccountIdService accountIdService = mock(AccountIdService.class);
        AccountStore accountStore = mock(AccountStore.class);
        when(accountStore.getAccountsForCustomer(eq(customerId))).thenReturn(List.of(account));

        AccountService accountService = new AccountService(accountIdService, accountStore);
        var accounts = accountService.getAccountsForCustomer(customerId);
        Assertions.assertEquals(1, accounts.size());
        Assertions.assertTrue(accounts.contains(account));

        verifyNoInteractions(accountIdService);
        verify(accountStore, times(1)).getAccountsForCustomer(customerId);
    }

}