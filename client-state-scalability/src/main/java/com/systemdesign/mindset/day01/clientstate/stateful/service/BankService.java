package com.systemdesign.mindset.day01.clientstate.stateful.service;

import com.systemdesign.mindset.day01.clientstate.stateful.store.InMemoryDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/*
    ACCOUNT STATE RAM'DE TUTULUYOR.
    SERVER RESTART OLURSA TÜM BAKİYELER SIFIRLANIR.
    DISTRIBUTED DEĞİL.
    SCALE EDİLEMEZ.
*/

@Service
@RequiredArgsConstructor
public class BankService {

    private final InMemoryDatabase memoryDatabase;

    public BigDecimal getBalance(Long userId) {
        return memoryDatabase.getAccountById(userId).getBalance();
    }

    public void withdraw(Long userId, BigDecimal amount) {
        memoryDatabase.getAccountById(userId).withdraw(amount);
    }

    public void deposit(Long userId, BigDecimal amount) {
        memoryDatabase.getAccountById(userId).deposit(amount);
    }
}
