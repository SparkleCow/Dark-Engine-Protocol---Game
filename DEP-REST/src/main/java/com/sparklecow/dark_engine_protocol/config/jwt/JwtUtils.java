package com.sparklecow.dark_engine_protocol.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {

    private final JwtProperties jwtProperties;

    public Authentication getAuthentication(String token) {
        Claims claims = extractAllClaims(token);

        if (isExpired(claims)) {
            throw new JwtException("JWT expired");
        }

        String subject = claims.getSubject();
        if (subject == null) {
            throw new JwtException("JWT subject missing");
        }

        Collection<SimpleGrantedAuthority> authorities =
                extractRoles(claims);

        return new UsernamePasswordAuthenticationToken(
                subject,
                null,
                authorities
        );
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(generateSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration == null || expiration.before(new Date());
    }

    @SuppressWarnings("unchecked")
    private Collection<SimpleGrantedAuthority> extractRoles(Claims claims) {
        Object rolesObj = claims.get("roles");

        if (rolesObj == null) {
            return List.of();
        }

        List<String> roles = (List<String>) rolesObj;

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private Key generateSignKey() {
        String secret = jwtProperties.getSecretKey();
        byte[] keyBytes;

        try {
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (IllegalArgumentException ex) {
            log.warn("JWT secret is not Base64 encoded, using raw bytes");
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
