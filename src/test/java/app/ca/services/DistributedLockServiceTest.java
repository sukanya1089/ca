package app.ca.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

class DistributedLockServiceTest {

    @Test
    @DisplayName("Distribute lock should respect lock semantics")
    void testLock() {
        Set<Integer> nonThreadSafeSet = new HashSet<>();
        IntStream.range(1, 10).forEach(nonThreadSafeSet::add);
        DistributedLockService lockService = new DistributedLockService();
        String lockId = UUID.randomUUID().toString();
        IntStream.range(1, 10).parallel().forEach(i -> removeFromSet(lockService, lockId, nonThreadSafeSet, i));
        Assertions.assertTrue(nonThreadSafeSet.isEmpty());
    }

    private static void removeFromSet(DistributedLockService lockService, String lockId, Set<Integer> nonThreadSafeSet, int i) {
        var lock = lockService.lock(lockId);
        try {
            lock.lock();
            nonThreadSafeSet.remove(i);
        } finally {
            lock.unlock();
        }
    }
}