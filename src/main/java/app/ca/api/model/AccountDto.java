package app.ca.api.model;

import lombok.*;
import org.javamoney.moneta.Money;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    @NonNull
    String accountId;
    @NonNull
    Money balance;

}
