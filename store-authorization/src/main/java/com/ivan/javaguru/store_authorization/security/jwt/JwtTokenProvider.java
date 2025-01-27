package com.ivan.javaguru.store_authorization.security.jwt;

import com.ivan.javaguru.store_authorization.persistence.model.RoleNameEnum;
import com.ivan.javaguru.store_authorization.usecasses.dto.AuthenticationUserDto;
import com.ivan.javaguru.store_authorization.usecasses.dto.TokenValidResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${spring.security.jwt.secret}")
    private String secret;

    @Value("${spring.security.jwt.expired}")
    private long validityInMilliseconds;

    public String createToken(AuthenticationUserDto user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", getRoleNames(user.getRoles().stream().toList()));

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(user.getEmail())
                .issuedAt(now)
                .expiration(validity)
                .and()
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey getKey() {
        byte[] secretBytes = this.secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(secretBytes);
    }

    private List<String> getRoleNames(List<RoleNameEnum> userRoles) {
        List<String> result = new ArrayList<>();
        userRoles.forEach(role -> result.add(role.name()));
        return result;
    }

    public TokenValidResponseDto isTokenValid(String token) {
        try {
            Claims claims = this.extractClaims(token);
            if (claims.getExpiration().before(new Date())) {
                log.info("IN validateToken - JWT token has expired");
                return TokenValidResponseDto.builder()
                        .isValid(false)
                        .message("JWT token has expired")
                        .build();
            }
            log.info("IN validateToken - JWT token valid");
            return TokenValidResponseDto.builder()
                    .isValid(true)
                    .message("JWT token valid")
                    .build();
        } catch (JwtException | IllegalArgumentException e) {
            log.info("IN validateToken  - expired or invalid JWT token");
            return TokenValidResponseDto.builder()
                    .isValid(false)
                    .message("Expired or invalid JWT token")
                    .build();
        }
    }

    private Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
