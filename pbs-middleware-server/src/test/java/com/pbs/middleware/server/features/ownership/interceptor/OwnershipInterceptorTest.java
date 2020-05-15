package com.pbs.middleware.server.features.ownership.interceptor;

import com.pbs.middleware.server.features.ownership.service.OwnershipService;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Connection service tests")
class OwnershipInterceptorTest {

    @Mock
    OwnershipService ownershipService;

    private static Stream<Arguments> provideStringsForIsBlank() {
        return Stream.of(
                Arguments.of("/jobs/{id}/details")
        );
    }

    @Before
    void beforeTest() {
        Mockito.reset(ownershipService);
    }

    @ParameterizedTest
    @MethodSource("provideStringsForIsBlank")
    void preHandle(String url) {
        UUID id = UUID.randomUUID();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("id", id.toString());
        request.setRequestURI(url);

        when(ownershipService.isAdmin()).thenReturn(true);

        Assert.assertTrue("fail", new OwnershipInterceptor(ownershipService)
                .preHandle(request, null, null));

    }
}