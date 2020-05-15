package com.pbs.middleware.server.common.security;

import com.pbs.middleware.server.common.security.jwt.AccountCredentials;
import com.pbs.middleware.server.features.documentation.TagsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Api(tags = TagsConfig.AUTH_TAG)
public class AuthController {

    /**
     * TODO replace with ApiListingScannerPlugin in {@link com.pbs.middleware.server.features.documentation.SpringfoxConfig#dynamicApis()}
     **/
    @ApiOperation("Login.")
    @PostMapping("/auth")
    public void fakeLogin(@ApiParam("User") @RequestBody AccountCredentials credentials) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

}
