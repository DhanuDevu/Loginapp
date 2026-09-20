package com.reglog.auth.repository;

import com.reglog.auth.model.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

    Optional<JwtToken> findByToken(String token);

    boolean existsByTokenAndEatAfter(String token, LocalDateTime now);

    void deleteByUid(Long uid);

    void deleteByToken(String token);
}
