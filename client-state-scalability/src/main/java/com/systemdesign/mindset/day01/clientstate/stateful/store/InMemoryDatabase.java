package com.systemdesign.mindset.day01.clientstate.stateful.store;

import com.systemdesign.mindset.day01.clientstate.stateful.model.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryDatabase {

    private final Map<Long, Account> accounts = new ConcurrentHashMap<>();

    public InMemoryDatabase() {
        accounts.put(1L, new Account(1L, BigDecimal.valueOf(1000)));
    }

    public Account getAccountById(Long id) {
        Account account = accounts.get(id);
        if (account == null) {
            throw new IllegalStateException("Account not found");
        }
        return account;
    }
}
