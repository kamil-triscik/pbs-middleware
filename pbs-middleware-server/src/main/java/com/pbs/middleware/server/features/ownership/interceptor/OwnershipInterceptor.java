package com.pbs.middleware.server.features.ownership.interceptor;

import com.pbs.middleware.api.job.ApiConfig;
import com.pbs.middleware.server.common.exception.AccessDeniedException;
import com.pbs.middleware.server.common.security.MiddlewareInterceptor;
import com.pbs.middleware.server.common.utils.Optional;
import com.pbs.middleware.server.features.ownership.service.OwnershipService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;

import static com.pbs.middleware.server.common.utils.Optional.ofNullable;
import static java.lang.String.format;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

@Slf4j
@Component
@RequiredArgsConstructor
public class OwnershipInterceptor extends MiddlewareInterceptor {

    private final OwnershipService ownerService;

    @Override
    protected List<String> getPathPatterns() {
        List<String> paths = new ArrayList<>(ApiConfig.forOwnerShip());
        paths.addAll(com.pbs.middleware.api.upload.ApiConfig.forOwnerShip());
        paths.addAll(com.pbs.middleware.api.download.ApiConfig.forOwnerShip());
        return ListUtils.unmodifiableList(paths);

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (ownerService.isAdmin()) {
            return true;
        }

        getPathId(request)
                .filter(ownerService::existsOwnerShip)
                .filter(id -> !ownerService.isOwner(id))
                .also(id -> log.warn(format("Invalid access request. User[%s] tried to access [%s]", ownerService.getLoggedUserUuid(), id)))
                .ifPresent(it -> { throw new AccessDeniedException("Access forbidden!"); });
        return true;
    }

    @SuppressWarnings("unchecked")
    private Optional<UUID> getPathId(HttpServletRequest request) {
        return ofNullable(request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE))
                .map(Map.class::cast)
                .map(map -> (Map<String, String>) map)
                .map(pathVariables -> pathVariables.get("id"))
                .map(UUID::fromString);
    }
}
