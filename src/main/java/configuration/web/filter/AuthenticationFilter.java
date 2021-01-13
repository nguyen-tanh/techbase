package configuration.web.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.techbase.application.AuthenticationService;
import com.techbase.support.exception.NotFoundTokenAuthentication;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author nguyentanh
 */
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER = "Bearer ";

    private final List<AntPathRequestMatcher> permitAllMatchers;
    private final AuthenticationService authenticationService;
    private final HandlerExceptionResolver resolver;

    public AuthenticationFilter(List<AntPathRequestMatcher> permitAllMatchers,
                                AuthenticationService authenticationService,
                                @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.permitAllMatchers = permitAllMatchers;
        this.authenticationService = authenticationService;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // Ignore checking authentication
        if (this.permitAllMatchers.stream().anyMatch(ant -> ant.matches(request))) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);


        if (StringUtils.isBlank(header) || !header.startsWith(BEARER)) {
            resolver.resolveException(request, response, null, new NotFoundTokenAuthentication("Header is invalid"));
            return;
        }

        try {
            String token = header.substring(BEARER.length());
            DecodedJWT decodedJWT = this.authenticationService.verify(token);

            String userName = this.authenticationService.getUserName(decodedJWT);
            String role = this.authenticationService.getRole(decodedJWT);
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, null, Collections.singleton(authority));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (JWTVerificationException e) {
            resolver.resolveException(request, response, null, new JWTVerificationException("Authentication is invalid"));
            return;
        }

        chain.doFilter(request, response);
    }
}
