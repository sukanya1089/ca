package app.ca.services;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service to generate globally Unique Account Id.
 * <br/>
 * Responsibilities:
 * <br/>
 * <ol>
 * <li>Generate next Account Id/Number</li>
 * <li>Reclaim missed Account Numbers.</li>
 * </ol>
 * <br/>
 * <string>Note:</string> Here we are just using a random String.
 */
@Service
public class AccountIdService {

    @NonNull
    public String nextAccountId() {
        return UUID.randomUUID().toString();
    }
}
