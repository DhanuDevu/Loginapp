package com.reglog.auth.service;

import com.reglog.auth.dto.AuthResponse;
import com.reglog.auth.dto.LoginRequest;
import com.reglog.auth.dto.TokenValidationResponse;

public interface AuthService {

    AuthResponse login(LoginRequest loginRequest);

    TokenValidationResponse validateToken(String token);

    void logout(String token);
}
