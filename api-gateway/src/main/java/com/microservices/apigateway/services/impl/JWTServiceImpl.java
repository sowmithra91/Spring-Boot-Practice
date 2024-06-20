package com.microservices.apigateway.services.impl;

import com.microservices.apigateway.services.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.security.StandardSecureDigestAlgorithms;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

@Service
public class JWTServiceImpl implements JWTService {

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder().subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .signWith(getSignature(), StandardSecureDigestAlgorithms.findBySigningKey(getSignature()))
                //.signWith(getSignature(), SignatureAlgorithm.ES256)
                .compact();
    }

    @Override
    public String extractUserName(String token) {
        return  extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith((SecretKey) getSignature()).build().parseSignedClaims(token).getPayload();
    }

    private Key getSignature() {
        byte[] key = Decoders.BASE64.decode("6767GJHGGFH7657657y#%#%^$jhhvhguy7u6 ");
        return Keys.hmacShaKeyFor(key);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userId = extractUserName(token);
        return (userId.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(Date.from(Instant.now()));
    }
}
