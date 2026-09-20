package com.reglog.user.service;

import com.reglog.user.dto.SignupRequest;
import com.reglog.user.dto.UserResponse;
import com.reglog.user.model.User;
import com.reglog.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserResponse registerUser(SignupRequest signupRequest) {
        // Check if username already taken
        if (userRepository.existsByName(signupRequest.getName())) {
            throw new IllegalArgumentException("User name '" + signupRequest.getName() + "' is already registered.");
        }

        // Check if email already taken
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new IllegalArgumentException("Email '" + signupRequest.getEmail() + "' is already registered.");
        }

        // Mandatory BCrypt Hashing: plain text passwords must NEVER be saved!
        String encryptedPassword = passwordEncoder.encode(signupRequest.getPassword());

        // Create new User entity with encrypted password
        User user = new User(
                signupRequest.getName(),
                encryptedPassword,
                signupRequest.getEmail(),
                signupRequest.getPhone_no()
        );

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getPhone_no()
        );
    }

    @Override
    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserResponse getUserProfile(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("User not found with name: " + name));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone_no()
        );
    }
}
