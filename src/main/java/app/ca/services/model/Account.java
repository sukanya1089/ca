package app.ca.services.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;


@Value
@Builder
public class Account {
    @NonNull
    String accountId;
    @NonNull
    String customerId;
    @NonNull
    Money balance;
    @org.springframework.lang.NonNull
    public CurrencyUnit getCurrency() {
        return balance.getCurrency();
    }
}
