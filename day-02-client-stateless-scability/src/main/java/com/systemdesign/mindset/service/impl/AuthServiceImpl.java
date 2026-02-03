package com.systemdesign.mindset.service.impl;

import com.systemdesign.mindset.dto.LoginRequest;
import com.systemdesign.mindset.dto.LoginResponse;
import com.systemdesign.mindset.model.User;
import com.systemdesign.mindset.security.JwtService;
import com.systemdesign.mindset.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {

        // 1. Authenticate
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        log.debug("✅ Authentication successful for: {}", request.username());

        // 2. Extract authenticated principal
        User user = (User) authentication.getPrincipal();

        // 3. Generate JWT
        String token = jwtService.generateToken(user);

        log.info("✅ Login successful. JWT generated for: {}", user.getUsername());

        return new LoginResponse(
                token,
                user.getUsername(),
                "Login successful - JWT token generated (Stateless!)"
        );
    }
}
