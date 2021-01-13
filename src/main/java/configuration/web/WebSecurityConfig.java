package configuration.web;

import com.techbase.application.AuthenticationService;
import configuration.web.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author nguyentanh
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationService authenticationService;
    private final HandlerExceptionResolver resolver;

    public WebSecurityConfig(AuthenticationService authenticationService,
                             @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.authenticationService = authenticationService;
        this.resolver = resolver;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<AntPathRequestMatcher> permitAllMatchers = Arrays.asList(
                new AntPathRequestMatcher("/"),
                new AntPathRequestMatcher("/api/auth"));

        List<AntPathRequestMatcher> userAuths = Collections.singletonList(
                new AntPathRequestMatcher("/api/user/**"));

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .authorizeRequests(author -> author
                        .antMatchers(permitAllMatchers.stream().map(AntPathRequestMatcher::getPattern).toArray(String[]::new)).permitAll()
                        .antMatchers(userAuths.stream().map(AntPathRequestMatcher::getPattern).toArray(String[]::new)).hasAnyAuthority("DIRECTOR", "MANAGER")
                        .anyRequest().authenticated()
                ).addFilterBefore(new AuthenticationFilter(permitAllMatchers, authenticationService, resolver), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exp -> exp.accessDeniedHandler((request, response, accessDeniedException) -> {
                    resolver.resolveException(request, response, null, accessDeniedException);
                }));
    }

    /**
     * Register Crypt Password
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
