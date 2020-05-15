package cz.muni.ll.middleware.client.authorization.components.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.ll.middleware.client.authorization.Credentials;
import cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable.ForbiddenException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

import static cz.muni.ll.middleware.client.rest.exceptions.Utils.getResponseBody;

@Component
public class JwtAuthorizationInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String EXPIRATION_CLAIM = "exp";

    private String token = null;
    private Date expiration = new Date();

    private final Credentials login;

    private final AuthorizationClient authorizationClient;

    public JwtAuthorizationInterceptor(Credentials login, AuthorizationClient authorizationClient) {
        this.login = login;
        this.authorizationClient = authorizationClient;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(AUTHORIZATION_HEADER, getToken());
    }

    private String getToken() {
        if (token == null || isExpired()) {
            fetchNewToken();
        }
        return token;
    }

    private boolean isExpired() {
        return new Date().after(this.expiration);
    }

    private void fetchNewToken() {
        Response response = authorizationClient.login(login);
        if (response.status() == 200) {
            this.token = response.headers().get(AUTHORIZATION_HEADER).iterator().next();
            fetchExpiration();
        } else {
            throw new ForbiddenException("Access token fetching failed: " + getResponseBody(response));
        }
    }

    private void fetchExpiration() {
        String[] split_string = token.split("\\.");
        String base64EncodedBody = split_string[1];
        String body = new String(Base64.getDecoder().decode(base64EncodedBody));
        try {
            Map<String, Object> result = new ObjectMapper().readValue(body, HashMap.class);
            long expirationValue = ((Integer) result.getOrDefault(EXPIRATION_CLAIM, -1)).longValue(); //todo error handling
            this.expiration = new Date(expirationValue * 1000L);
        } catch (Exception e) {
            e.printStackTrace(); //todo
        }

    }

}
