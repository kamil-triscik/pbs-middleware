package cz.muni.ll.middleware.client.authorization.components;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthorizationInterceptor implements RequestInterceptor {

    final private RequestInterceptor requestInterceptor;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestInterceptor.apply(requestTemplate);
    }

    public static AuthorizationInterceptor of(RequestInterceptor interceptor) {
        return new AuthorizationInterceptor(interceptor);
    }
}
