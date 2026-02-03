package com.systemdesign.mindset.service;

import com.systemdesign.mindset.dto.LoginRequest;
import com.systemdesign.mindset.dto.LoginResponse;

public interface AuthService {

    LoginResponse login (LoginRequest loginRequest);
}
