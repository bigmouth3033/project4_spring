package com.service.main.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value(value = "${jwt.token.secret}")
    private String SIGN_KEY;


    public String extractId(String token) {
        return extractClaim(token, Claims::getId);
    }

    public String extractIssuer(String token){
        return extractClaim(token, Claims::getIssuer);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            String email,
            String issuer

    ) {
        long JWT_EXPIRATION = 1000 * 60 * 60 * 24;
        return Jwts
                .builder()
                .id(email)
                .issuer(issuer)
                .claims(extraClaims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean validateToken(String token) {
        var claims = extractAllClaims(token);
        return claims != null;
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SIGN_KEY);
        return Keys.hmacShaKeyFor(keyBytes);

    }
}
