package com.systemdesign.mindset.day01.clientstate.stateful.service;

import com.systemdesign.mindset.day01.clientstate.stateful.dto.TransferRequest;
import com.systemdesign.mindset.day01.clientstate.stateful.model.User;
import com.systemdesign.mindset.day01.clientstate.stateful.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * User Service
 * İş mantığı burada
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    /**
     * Kullanıcı doğrulama (Authentication)
     * Gerçek projede password hash karşılaştırması yapılır!
     */
    public User authenticate(String username, String password) {
        log.debug("Authenticating user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // ⚠️ Basit karşılaştırma - Gerçek projede BCrypt kullanılmalı!
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        log.info("User authenticated successfully: {}", username);
        return user;
    }

    /**
     * Username ile kullanıcı bul
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    /**
     * Bakiye sorgula
     */
    public BigDecimal getBalance(String username) {
        User user = findByUsername(username);
        log.debug("Balance for {}: {}", username, user.getBalance());
        return user.getBalance();
    }

    /**
     * Transfer işlemi
     */
    @Transactional
    public void transfer(String fromUsername, TransferRequest request) {
        log.info("Transfer: {} -> {} amount: {}",
                fromUsername, request.getToUsername(), request.getAmount());

        // Gönderen kullanıcı
        User fromUser = findByUsername(fromUsername);

        // Alıcı kullanıcı
        User toUser = findByUsername(request.getToUsername());

        // Kendine transfer kontrolü
        if (fromUsername.equals(request.getToUsername())) {
            throw new RuntimeException("Cannot transfer to yourself");
        }

        // Bakiye kontrolü
        if (fromUser.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Transfer işlemi
        fromUser.setBalance(fromUser.getBalance().subtract(request.getAmount()));
        toUser.setBalance(toUser.getBalance().add(request.getAmount()));

        userRepository.save(fromUser);
        userRepository.save(toUser);

        log.info("Transfer completed successfully");
    }
}
