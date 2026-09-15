package practice.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import practice.auth.entity.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    private final String secretKey;
    private final Long expiration;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.secretKey = secretKey;
        this.expiration = expiration;
    }

    private SecretKey getSigningKey(String key) {
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    // Generate token
    public String generateToken(User user) {

        List<String> roles = user
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .claims(Map.of(
                        "id", user.getId().toString(),
                        "roles", roles
                ))
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    // Parse Token
    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Get username
    public String getUsername(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    // Get User id
    public String getUserId(String token) {
        Claims claims = parseToken(token);
        return claims.get("id", String.class);
    }


    public boolean isExpiredToken(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().before(new Date());
    }

    public boolean isValidToken(User user, String token) {
        String email = getUsername(token);
        return (user.getUsername().equals(email) && !isExpiredToken(token));
    }
}