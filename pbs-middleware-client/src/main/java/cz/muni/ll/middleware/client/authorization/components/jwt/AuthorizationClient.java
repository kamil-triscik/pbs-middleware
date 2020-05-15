package cz.muni.ll.middleware.client.authorization.components.jwt;

import cz.muni.ll.middleware.client.authorization.Credentials;
import feign.Headers;
import feign.RequestLine;
import feign.Response;

public interface AuthorizationClient {

    @RequestLine("POST /auth")
    @Headers("Content-Type: application/json")
    Response login(Credentials login);

}
