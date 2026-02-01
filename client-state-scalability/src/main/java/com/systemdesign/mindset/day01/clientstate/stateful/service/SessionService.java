package com.systemdesign.mindset.day01.clientstate.stateful.service;

import com.systemdesign.mindset.day01.clientstate.stateful.session.SessionKeys;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Objects;

/*
    BUSINESS LOGIC BURADA AMA STATE SESSION DA.
    STATE : RAM'DE
    SERVER RESTART OLUNCA --> HERŞEY GİDER
    BAŞKA SERVER --> SESSION YOK.
    SESSION İÇERİSİNDE SADECE USER ID TUTULUYOR.
 */

@Service
public class SessionService {

    public void login(HttpSession session, Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is null");
        }
        session.setAttribute(SessionKeys.USER_ID, userId);
    }

    public Long getUserId(HttpSession session) {
        Object value =  session.getAttribute(SessionKeys.USER_ID);

        if (value == null) {
            throw new IllegalStateException("User not logged in");
        }

        if (!(value instanceof Long userId)) {
            throw new IllegalStateException("Invalid session data type for USER_ID");
        }
        return userId;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}
