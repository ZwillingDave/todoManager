package com.example.todoManager.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
// TODO: change secret
@Service
public class JWTWrapper {

    @Value("${jwt.secret}")
    private String secret;

    public String createJWTToken(String uuid, String username, Instant expiryDate) throws JWTCreationException {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withClaim("uuid", uuid)
                .withClaim("username", username)
                .withExpiresAt(expiryDate)
                .sign(algorithm);
    }
}
