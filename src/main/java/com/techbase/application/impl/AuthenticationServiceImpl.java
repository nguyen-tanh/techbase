package com.techbase.application.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.techbase.application.AuthenticationService;
import com.techbase.domain.Role;
import configuration.web.JWTConfig;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author nguyentanh
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final Algorithm algorithm;
    private final JWTConfig.JWTProperties jwtProperties;
    private final JWTVerifier jwtVerifier;

    public AuthenticationServiceImpl(Algorithm algorithm, JWTConfig.JWTProperties jwtProperties, JWTVerifier jwtVerifier) {
        this.algorithm = algorithm;
        this.jwtProperties = jwtProperties;
        this.jwtVerifier = jwtVerifier;
    }

    @Override
    public String createToken(String userName, Role role, Duration expiredAt) {

        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(expiredAt.getSeconds());
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

        return JWT.create()
                .withIssuer(jwtProperties.getIssuer())
                .withSubject(jwtProperties.getSubject())
                .withAudience(jwtProperties.getAudience())
                .withExpiresAt(date)
                .withClaim("userName", userName)
                .withClaim("role",
                        String.join(",", role.toString()))
                .sign(algorithm);
    }

    @Override
    public DecodedJWT verify(String refreshToken) {
        return jwtVerifier.
                verify(refreshToken);
    }

    @Override
    public String getUserName(DecodedJWT decodedJWT) {
        return decodedJWT
                .getClaim("userName")
                .asString();
    }

    @Override
    public String getRole(DecodedJWT decodedJWT) {
        return decodedJWT
                .getClaim("role")
                .asString();
    }
}
