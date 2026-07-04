package com.luftborn.luftborn_backend.security;

import com.luftborn.luftborn_backend.dto.response.RotatedToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Optional;

/*
* Generates SecureRandom opaque refresh token stored in Redis, httpOnly cookie for XSS, sameSite strict for CSRF
* Rotates refresh token once it has been used
* Old refresh token has a grace period of 3 seconds for network latency issues
* Upon logout the refresh token is deleted from Redis and the client receives an empty cookie
*/

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
    private final StringRedisTemplate redisTemplate;
    @Value("${security.jwt.refresh-token.expiration-ms}")
    private long refreshTokenDurationMs;
    @Value("${security.jwt.cookie-name}")
    private String cookieName;
    @Value("${security.jwt.cookie-secure}")
    private boolean cookieSecure;
    private final SecureRandom secureRandom = new SecureRandom();

    public String createRefreshToken(Long userId) {
        String rawToken = generateRawToken();
        String hashedToken = hashToken(rawToken);

        redisTemplate.opsForValue().set(
                hashedToken,
                String.valueOf(userId),
                Duration.ofMillis(refreshTokenDurationMs)
        );
        return rawToken;
    }

    public Optional<RotatedToken> rotateToken(String oldRawToken) {
        if (oldRawToken == null || oldRawToken.isBlank()) return Optional.empty();
        String oldHashedToken = hashToken(oldRawToken);
        String userIdStr = redisTemplate.opsForValue().getAndDelete(oldHashedToken);
        //Check if the current refresh token is empty
        if (userIdStr == null) {
            //Get the new token value stored with the revoked token key
            String existingNewData = redisTemplate.opsForValue().get("revoked:" +oldHashedToken);
            //Check if the new token value exists to return it
            if (existingNewData != null) {
                String[] parts = existingNewData.split(":");
                String cachedNewToken = parts[0];
                Long cachedUserid = Long.parseLong(parts[1]);
                return Optional.of(new RotatedToken(cachedNewToken, cachedUserid));
            }
            //New token value missing from the revoked token key (No refresh made/Breach)
            log.warn(oldHashedToken);
            return Optional.empty();
        }

        long userId;
        try {
            userId = Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
        String newRawToken = createRefreshToken(userId);

        //Revoke the old token for 3 seconds and reference the new token for future network issues
        redisTemplate.opsForValue().set(
                "revoked:"+oldHashedToken,
                newRawToken + ":" + userId,
                Duration.ofSeconds(3)
        );

        return Optional.of(new RotatedToken(newRawToken, userId));
    }

    //Generates a SecureRandom token
    private String generateRawToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public void logout(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) return;
        String hashedToken = hashToken(rawToken);
        redisTemplate.delete(hashedToken);
        redisTemplate.delete("revoked:"+hashedToken);
    }

    //Hash the raw token
    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseCookie generateRefreshTokenCookie(String refreshToken) {
        return buildCookie(refreshToken, refreshTokenDurationMs / 1000);
    }
    public ResponseCookie generateEmptyCookie() {
        return buildCookie("", 0);
    }
    private ResponseCookie buildCookie(String value, long maxAgeSec) {
        return ResponseCookie.from(cookieName, value)
                .path("/api/v1/auth")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Strict")
                .maxAge(maxAgeSec)
                .build();
    }
}
