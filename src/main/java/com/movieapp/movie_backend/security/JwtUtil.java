package com.movieapp.movie_backend.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
@Component
public class JwtUtil {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 86400000;
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println(" The token has expired");
        } catch (UnsupportedJwtException e) {
            System.out.println(" Token type is not supported");
        } catch (MalformedJwtException e) {
            System.out.println(" The token is invalid !");
        } catch (SignatureException e) {
            System.out.println(" The signature is invalid");
        } catch (IllegalArgumentException e) {
            System.out.println(" The token is empty or not valid");
        }
        return false;
    }
}
