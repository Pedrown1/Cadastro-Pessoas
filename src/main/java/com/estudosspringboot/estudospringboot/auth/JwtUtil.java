package com.estudosspringboot.estudospringboot.auth;

import com.estudosspringboot.estudospringboot.utils.Utilidades;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.SecretKey;
import java.math.BigDecimal;
import java.util.Date;

public class JwtUtil {

    @Autowired
    private static Utilidades util;

    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 minutos
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public static String validateToken(String token) {

        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
