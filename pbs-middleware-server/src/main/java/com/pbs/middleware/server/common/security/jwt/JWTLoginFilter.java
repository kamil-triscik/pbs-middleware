package com.pbs.middleware.server.common.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static java.util.Collections.emptyList;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;
    private TokenAuthenticationService tokenAuthenticationService;

    public JWTLoginFilter(String url, AuthenticationManager authManager, ObjectMapper objectMapper,
                          TokenAuthenticationService tokenAuthenticationService) {
        super(new AntPathRequestMatcher(url));
        this.tokenAuthenticationService = tokenAuthenticationService;
        setAuthenticationManager(authManager);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        AccountCredentials credentials = objectMapper.readValue(request.getInputStream(), AccountCredentials.class);

        return getAuthenticationManager().authenticate(getAuthentication(credentials));
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain,
            Authentication authentication) {

        tokenAuthenticationService.addAuthentication(response, authentication);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(AccountCredentials credentials) {
        return new UsernamePasswordAuthenticationToken(
                credentials.getEmail(),
                credentials.getPassword(),
                emptyList()
        );
    }
}
