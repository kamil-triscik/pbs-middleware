package com.pbs.middleware.server.common.security.jwt;

import com.pbs.middleware.server.common.security.JwtProperties;
import com.pbs.middleware.server.common.security.SecurityProperties;
import com.pbs.middleware.server.common.security.SecurityUser;
import com.pbs.middleware.server.common.security.SecurityUserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenAuthenticationServiceImpl implements TokenAuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationServiceImpl.class);

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final SecurityProperties properties;
    private final JwtProperties jwtProperties;

    @Override
    public void addAuthentication(HttpServletResponse response, Authentication authentication) {
        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(TokenClaims.ROLES, authentication.getAuthorities())
                .claim(TokenClaims.USER_ID, ((SecurityUser) authentication.getPrincipal()).getId())
                .claim(TokenClaims.FIRST_NAME, ((SecurityUser) authentication.getPrincipal()).getFirstName())
                .claim(TokenClaims.LAST_NAME, ((SecurityUser) authentication.getPrincipal()).getLastName())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration().toMillis()))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretkey())
                .compact();
        response.addHeader(AUTH_HEADER, TOKEN_PREFIX + token);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Authentication getAuthentication(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            var token = authHeader.replace(TOKEN_PREFIX, "");
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtProperties.getSecretkey())
                        .parseClaimsJws(token)
                        .getBody();

                var user = SecurityUserMapper.mapClaimsToSecurityUser(claims);
                return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            } catch (JwtException exception) {
                log.warn(String.format("Invalid token %s", token), exception);
            }
        }
        return null;
    }
}
