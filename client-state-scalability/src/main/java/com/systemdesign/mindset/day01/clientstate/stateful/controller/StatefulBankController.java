package com.systemdesign.mindset.day01.clientstate.stateful.controller;

import com.systemdesign.mindset.day01.clientstate.common.response.ApiResponse;
import com.systemdesign.mindset.day01.clientstate.stateful.service.BankService;
import com.systemdesign.mindset.day01.clientstate.stateful.service.SessionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/stateful/bank")
@RequiredArgsConstructor
public class StatefulBankController {

    private final BankService bankService;
    private final SessionService sessionService;

    @GetMapping("/balance")
    public ApiResponse<BigDecimal> getBalance(HttpSession session) {
        Long userId = sessionService.getUserId(session);
        BigDecimal balance = bankService.getBalance(userId);
        return new ApiResponse<>(true, balance, "Balance fetched successfully");
    }

    @GetMapping("/session-info")
    public ApiResponse<String> info(HttpSession session) {
        return new ApiResponse<>(true, session.getId(), "Session ID");
    }

    @PostMapping("/withdraw")
    public ApiResponse<String> withdraw(HttpSession session, @RequestParam("amount") BigDecimal amount) {
        Long userId = sessionService.getUserId(session);
        bankService.withdraw(userId, amount);
        return new ApiResponse<>(true, null, "Withdrawal successful");
    }

    @PostMapping("/deposit")
    public ApiResponse<String> deposit(HttpSession session, @RequestParam BigDecimal amount) {
        Long userId = sessionService.getUserId(session);
        bankService.deposit(userId, amount);
        return new ApiResponse<>(true, null, "Deposit successful");
    }
}