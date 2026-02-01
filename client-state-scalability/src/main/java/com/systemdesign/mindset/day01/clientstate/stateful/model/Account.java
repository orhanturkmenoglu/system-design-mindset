package com.systemdesign.mindset.day01.clientstate.stateful.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Long id;
    private BigDecimal balance;

    public void withdraw(BigDecimal amount) {
       if(amount.compareTo(BigDecimal.ZERO) < 0){
           throw  new IllegalArgumentException("Amount must be positive");
       }

       if(balance.compareTo(amount)<0){
           throw new IllegalStateException("Insufficient balance");
       }

       balance = balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) < 0){
            throw  new IllegalArgumentException("Amount must be positive");
        }

        balance = balance.add(amount);
    }
}
