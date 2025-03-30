package com.mysoch.service;

import com.mysoch.dto.AuthResponse;
import com.mysoch.dto.LoginRequest;
import com.mysoch.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
