package com.systemdesign.mindset.day01.clientstate.stateful.controller;

import com.systemdesign.mindset.day01.clientstate.common.response.ApiResponse;
import com.systemdesign.mindset.day01.clientstate.stateful.service.SessionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/stateful/auth")
@RequiredArgsConstructor
public class StatefulAuthController {

    private final SessionService sessionService;

    @PostMapping("/login")
    public ApiResponse<String> login(HttpSession session) {
        sessionService.login(session, 1L);
        return new ApiResponse<>(true, null, "Login successful");
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpSession session) {
        sessionService.logout(session);
        return new ApiResponse<>(true, null, "Logout successful");
    }
}
