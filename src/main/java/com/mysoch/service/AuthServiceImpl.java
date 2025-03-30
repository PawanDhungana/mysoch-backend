package com.mysoch.service;

import com.mysoch.dto.AuthResponse;
import com.mysoch.dto.LoginRequest;
import com.mysoch.dto.RegisterRequest;
import com.mysoch.model.User;
import com.mysoch.repository.UserRepository;
import com.mysoch.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //hashes password, saves user, returns JWT token
    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return new AuthResponse(null, "Username is already taken.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(null, "Email is already registered.");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponse(token, "User registered successfully.");
    }

    //verifies credentials and return JWT token
    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password.");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return new AuthResponse(token, "Login successful.");
    }
}
