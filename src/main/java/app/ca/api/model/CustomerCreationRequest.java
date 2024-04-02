package app.ca.api.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreationRequest {
    @NonNull
    String name;
    @NonNull
    String surname;
}
