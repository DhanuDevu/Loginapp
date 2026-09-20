package com.reglog.auth.service;

import com.reglog.auth.dto.AuthResponse;
import com.reglog.auth.dto.LoginRequest;
import com.reglog.auth.dto.TokenValidationResponse;
import com.reglog.auth.model.JwtToken;
import com.reglog.auth.repository.JwtTokenRepository;
import com.reglog.auth.util.JwtUtils;
import com.reglog.user.model.User;
import com.reglog.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenRepository jwtTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthServiceImpl(
            UserRepository userRepository,
            JwtTokenRepository jwtTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtTokenRepository = jwtTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        // 1. Fetch user by user name
        User user = userRepository.findByName(loginRequest.getName())
                .orElseThrow(() -> new BadCredentialsException("Invalid user name or password."));

        // 2. BCrypt password verification (comparing plain text input against stored BCrypt hash)
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid user name or password.");
        }

        // 3. JWT generation
        String token = jwtUtils.generateToken(user.getId(), user.getName());
        LocalDateTime cat = jwtUtils.getCreatedAtFromToken(token);
        LocalDateTime eat = jwtUtils.getExpiresAtFromToken(token);

        // 4. JWT token storage using the JWT_tokens table (tid, uid, token, cat, eat)
        JwtToken jwtToken = new JwtToken(user.getId(), token, cat, eat);
        jwtTokenRepository.save(jwtToken);

        // 5. Return AuthResponse
        return new AuthResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone_no()
        );
    }

    @Override
    public TokenValidationResponse validateToken(String token) {
        // 1. Cryptographic validation of signature and expiration
        if (!jwtUtils.validateToken(token)) {
            return new TokenValidationResponse(false, null, null, "Invalid or expired JWT token signature.");
        }

        // 2. Database validation using JWT_tokens table
        Optional<JwtToken> tokenOpt = jwtTokenRepository.findByToken(token);
        if (tokenOpt.isEmpty()) {
            return new TokenValidationResponse(false, null, null, "Token not found in active session store.");
        }

        JwtToken storedToken = tokenOpt.get();
        if (storedToken.getEat().isBefore(LocalDateTime.now())) {
            return new TokenValidationResponse(false, null, null, "Token has expired.");
        }

        String username = jwtUtils.getUsernameFromToken(token);
        Long uid = storedToken.getUid();

        return new TokenValidationResponse(true, username, uid, "Token is valid.");
    }

    @Override
    @Transactional
    public void logout(String token) {
        if (token != null && !token.isBlank()) {
            jwtTokenRepository.deleteByToken(token);
        }
    }
}
