package com.jendo.app.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jendo.app.domain.user.entity.OtpToken;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findByEmailAndOtp(String email, String otp);
    Optional<OtpToken> findByEmail(String email);
    void deleteByEmail(String email);
}
