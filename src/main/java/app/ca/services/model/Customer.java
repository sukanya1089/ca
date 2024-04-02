package app.ca.services.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;


@Value
@Builder
public class Customer {
    @NonNull
    String customerId;
    @NonNull
    String name;
    @NonNull
    String surname;
}
