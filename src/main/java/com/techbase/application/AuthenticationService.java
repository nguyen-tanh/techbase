package com.techbase.application;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.techbase.domain.Role;

import java.time.Duration;

/**
 * @author nguyentanh
 */
public interface AuthenticationService {

    String createToken(String userName, Role role, Duration expiredAt);

    DecodedJWT verify(String refreshToken);

    String getUserName(DecodedJWT decodedJWT);

    String getRole(DecodedJWT decodedJWT);
}
