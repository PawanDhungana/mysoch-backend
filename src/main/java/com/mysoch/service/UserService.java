package com.mysoch.service;

import com.mysoch.model.User;

import java.util.Optional;

public interface UserService {

    User saveUser(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
