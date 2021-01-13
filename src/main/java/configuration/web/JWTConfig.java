package configuration.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author nguyentanh
 */
@Configuration
public class JWTConfig {

    @Bean
    public Algorithm jwtAlgorithm(JWTProperties jwtProperties) {
        return Algorithm.HMAC256(jwtProperties.secret);
    }

    @Bean
    public JWTVerifier verifier(Algorithm algorithm, JWTProperties jwtProperties) {
        return JWT
                .require(algorithm)
                .withIssuer(jwtProperties.issuer)
                .build();
    }

    @Component
    @ConfigurationProperties(prefix = "jwt")
    @Data
    public static class JWTProperties {
        private String secret;
        private String issuer;
        private String subject;
        private String audience;
    }

    @Component
    @ConfigurationProperties(prefix = "jwt.expired")
    @Data
    public static class JWTExpired {
        private Duration accessToken;
        private Duration refreshToken;
    }
}
