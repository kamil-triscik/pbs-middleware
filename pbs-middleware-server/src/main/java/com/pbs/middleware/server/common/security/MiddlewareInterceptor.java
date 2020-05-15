package com.pbs.middleware.server.common.security;

import java.util.List;
import org.springframework.web.servlet.HandlerInterceptor;

import static java.util.Collections.emptyList;

public abstract class MiddlewareInterceptor implements HandlerInterceptor {

    protected List<String> getPathPatterns() {
        return emptyList();
    }

    protected List<String> getExcludedPatterns() {
        return emptyList();
    }

}
