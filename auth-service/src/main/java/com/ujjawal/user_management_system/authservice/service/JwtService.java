package com.ujjawal.user_management_system.authservice.service;

//import com.ujjawal.user_management_system.authservice.security.UserPrinciple;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    // Inject the JWT secret key from the properties file
    @Value("${jwt.secret}")
    private String secretKey;

//    private final String secretKey;
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

//    public JwtService(){
//        secretKey = generateSecretKey();
//    }

//    public String generateSecretKey() {
//        try {
//            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey secretKey = keyGen.generateKey();
//            System.out.println("Secret Key : " + secretKey.toString());
//            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Error generating secret key", e);
//        }
//    }

    public String generateToken(String userId, String tokenType) {
        Map<String, Object> claims = new HashMap<>();
        String expTime = String.valueOf(System.currentTimeMillis());
        if (Objects.equals(tokenType, "accessToken")) {
            expTime = String.valueOf(System.currentTimeMillis() + 1000*60*30);
        } else if (Objects.equals(tokenType, "refreshToken")) {
            expTime = String.valueOf(System.currentTimeMillis() + 1000*60*60*24*15);
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(Long.parseLong(expTime)))
                .signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserId(String token) {
        // extract the user id from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();
    }


    public Map<String, Object> validateToken(String token) {
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = extractUserId(token);  // This extracts the subject (user ID)
            boolean isExpired = isTokenExpired(token);
            result.put("userId", userId);
            result.put("isExpired", isExpired);
            result.put("isValid", userId != null && !isExpired);
        } catch (ExpiredJwtException e) {
            logger.warn("The token is expired", e);
            result.put("userId", null);
            result.put("isExpired", true);
            result.put("isValid", false);
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: " + e.getMessage());
            result.put("userId", null);
            result.put("isExpired", false);
            result.put("isValid", false);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token format: " + e.getMessage());
            result.put("userId", null);
            result.put("isExpired", false);
            result.put("isValid", false);
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: " + e.getMessage());
            result.put("userId", null);
            result.put("isExpired", false);
            result.put("isValid", false);
        } catch (IllegalArgumentException e) {
            logger.error("JWT token is invalid: " + e.getMessage());
            result.put("userId", null);
            result.put("isExpired", false);
            result.put("isValid", false);
        }
        return result;
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
