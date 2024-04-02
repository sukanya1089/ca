package app.ca.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

class AccountIdServiceTest {

    @Test
    @DisplayName("nextAccountId should return non null account id")
    void testNextAccountIdForNonNull() {
        AccountIdService accountIdService = new AccountIdService();
        Assertions.assertNotNull(accountIdService.nextAccountId());
    }

    @Test
    @DisplayName("nextAccountId should return unique account id")
    void testNextAccountIdForUniqueness() {
        AccountIdService accountIdService = new AccountIdService();
        Set<String> seenAccountIds = ConcurrentHashMap.newKeySet();
        IntStream.range(1, 100).parallel().forEach(i -> {
            String nextAccountId = accountIdService.nextAccountId();
            Assertions.assertFalse(seenAccountIds.contains(nextAccountId));
            seenAccountIds.add(nextAccountId);
        });
    }

}