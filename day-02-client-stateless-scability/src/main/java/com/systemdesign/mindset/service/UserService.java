package com.systemdesign.mindset.service;

import com.systemdesign.mindset.dto.TransferRequest;
import com.systemdesign.mindset.model.User;
import com.systemdesign.mindset.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * User Service
 */
public interface UserService {

    User findByUsername(String username);

    BigDecimal getBalance(String username);

    void transfer(String fromUsername, TransferRequest requestDTO);
}