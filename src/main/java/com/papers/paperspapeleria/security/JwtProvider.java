package com.papers.paperspapeleria.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecretString; // La clave del .yml

    private int jwtExpirationMs = 86400000; // 1 día

    // 1. ⚠️ MÉTODO DE AYUDA para crear la clave
    // Esto garantiza que la clave se genere IDÉNTICA cada vez
    private SecretKey getSigningKey() {
        byte[] keyBytes = this.jwtSecretString.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    // Método para generar el token
    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        String username = userPrincipal.getUsername();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                
                // 2. ⚠️ Usamos la clave generada y FORZAMOS HS256
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) 
                
                .compact();
    }

    // Método para obtener el username (identificacion) desde el token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                // 3. ⚠️ Usamos la misma clave generada
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Método para validar el token
    public boolean validateToken(String authToken) {
        try {
            // 4. ⚠️ Usamos la misma clave generada
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            // Este es el error que estás viendo
            logger.error("Firma JWT inválida: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Token JWT inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string está vacío: {}", e.getMessage());
        }
        return false;
    }
}