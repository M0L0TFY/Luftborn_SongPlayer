package com.luftborn.luftborn_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
* Generates stateless JWT access token
* JWT Payload subject is the userId (Primary Key), claims are the username and email
*/

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Value("${security.jwt.expiration-ms}")
    private long jwtExpirationMs;
    @Value("${security.jwt.issuer}")
    private String issuer;

    public String generateJwtToken(UserDetailsImpl principal) {
        return buildJwtToken(principal);
    }

    private String buildJwtToken(UserDetailsImpl principal) {
        Date now = new Date();
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", principal.getUsername());
        claims.put("email", principal.email());
        return Jwts.builder()
                .issuer(issuer)
                .subject(String.valueOf(principal.id()))
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtExpirationMs))
                .signWith(getSignInKey())
                .compact(); //Produces the HMAC by combining jwt header + payload + secretKey
    }

    public boolean isTokenValid(String token, @NonNull UserDetailsImpl userDetails) {
        try {
            Long id = extractSubject(token);
            return id.equals(userDetails.id());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //Extract User ID stored in payload subject
    public Long extractSubject(String token) {
        try {
            return Long.parseLong(extractAllClaims(token).getSubject());
        } catch (NumberFormatException e) {
            throw new MalformedJwtException("Invalid JWT token subject", e);
        }
    }
    public String extractUsername(String token) {
        return extractAllClaims(token).get("username", String.class);
    }
    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    //Extracts the jwt payload from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //Decodes the HMAC + Base64 generated secretKey
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
