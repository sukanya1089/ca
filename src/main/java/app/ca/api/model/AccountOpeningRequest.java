package app.ca.api.model;

import lombok.*;
import org.javamoney.moneta.Money;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountOpeningRequest {
    @NonNull
    String customerId;
    @NonNull
    Money initialAmount;
}
