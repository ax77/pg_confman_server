package com.pc_builder.security.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.pc_builder.security.service.UserDetailsImpl;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenGenerator {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private int expired;

    public String generateToken(Authentication auth) {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        JwtBuilder builder = Jwts.builder();
        builder.setSubject(userDetails.getName());
        builder.setIssuedAt(new Date());
        builder.setExpiration(new Date(new Date().getTime() + expired));
        builder.signWith(SignatureAlgorithm.HS512, secret);

        return builder.compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJwt(token);
            return true;
        } catch (Exception e) {
            // TODO: LOGGING
            System.out.println(e.toString());
        }
        return false;
    }
}
