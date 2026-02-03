package com.systemdesign.mindset.service.impl;

import com.systemdesign.mindset.dto.TransferRequest;
import com.systemdesign.mindset.model.User;
import com.systemdesign.mindset.repository.UserRepository;
import com.systemdesign.mindset.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    @Override
    public BigDecimal getBalance(String username) {
        User user = findByUsername(username);
        log.debug("Balance for {}: {}", username, user.getBalance());
        return user.getBalance();
    }

    @Override
    @Transactional
    public void transfer(String fromUsername, TransferRequest requestDTO) {
        log.info("Transfer: {} -> {} amount: {}",
                fromUsername, requestDTO.toUsername(), requestDTO.amount());

        User fromUser = findByUsername(fromUsername);
        User toUser = findByUsername(requestDTO.toUsername());

        if (fromUsername.equals(requestDTO.toUsername())) {
            throw new RuntimeException("Cannot transfer to yourself");
        }

        if (fromUser.getBalance().compareTo(requestDTO.amount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        fromUser.setBalance(fromUser.getBalance().subtract(requestDTO.amount()));
        toUser.setBalance(toUser.getBalance().add(requestDTO.amount()));

        userRepository.save(fromUser);
        userRepository.save(toUser);

        log.info("Transfer completed successfully");
    }
}