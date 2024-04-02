package app.ca.services;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Locking Service to execute <b>delta</b> operations like credit/debit.
 * This should be replaced with distributed locks like Redis/DB level locks....
 */
@Service
public class DistributedLockService {

    private final Map<String, Lock> locks = new ConcurrentHashMap<>();

    @NonNull
    public Lock lock(@NonNull String accountId) {
        return locks.computeIfAbsent(accountId, (k) -> new ReentrantLock(true));
    }
}
