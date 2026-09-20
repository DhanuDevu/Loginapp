package com.reglog.user.service;

import com.reglog.user.dto.SignupRequest;
import com.reglog.user.dto.UserResponse;
import com.reglog.user.model.User;

import java.util.Optional;

public interface UserService {

    UserResponse registerUser(SignupRequest signupRequest);

    Optional<User> findByName(String name);

    Optional<User> findById(Long id);

    UserResponse getUserProfile(String name);
}
