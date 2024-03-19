package com.extremecloud.cis.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author dprasad
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable() // Disable CSRF protection for simplicity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .anyRequest().authenticated() // Require authentication for all requests
                .and()
                .httpBasic().and()
                .headers().cacheControl().disable() // Disable client-side caching of responses
                .and()// Disable CSRF protection for simplicity
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomBasicAuthenticationEntryPoint();
    }

    static class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
        private static final String AUTH_ERROR_MESSAGE =
                "{\"error\": \"Unauthorized\", \"message\": \"Bad Credentials\"}";

        public CustomBasicAuthenticationEntryPoint() {
            // Set the realm name for Basic Authentication
            setRealmName("Basic Auth");
        }

        @Override
        public void commence(
                HttpServletRequest request,
                HttpServletResponse response,
                org.springframework.security.core.AuthenticationException authException)
                throws IOException {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.println(AUTH_ERROR_MESSAGE);
        }
    }
}