package com.pbs.middleware.server.features.documentation;

import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.pbs.middleware.server.MiddlewareServer;
import com.pbs.middleware.server.common.security.AuthenticationType;
import com.pbs.middleware.server.common.security.jwt.AccountCredentials;
import com.pbs.middleware.server.features.connection.rest.ConnectionsController;
import com.pbs.middleware.server.features.contact.rest.ContactController;
import com.pbs.middleware.server.features.filetransfer.download.rest.DownloadController;
import com.pbs.middleware.server.features.filetransfer.upload.rest.UploadController;
import com.pbs.middleware.server.features.job.rest.JobsController;
import com.pbs.middleware.server.features.script.rest.ScriptController;
import com.pbs.middleware.server.features.template.rest.TemplateController;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.not;
import static com.pbs.middleware.api.connection.ApiConfig.CONNECTIONS;
import static com.pbs.middleware.api.contact.ApiConfig.CONTACTS;
import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_PREFIX;
import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_PREFIX;
import static com.pbs.middleware.api.job.ApiConfig.JOB_PREFIX;
import static com.pbs.middleware.api.script.ApiConfig.SCRIPTS;
import static com.pbs.middleware.api.template.ApiConfig.TEMPLATES;
import static com.pbs.middleware.api.user.ApiConfig.USERS;
import static com.pbs.middleware.server.common.security.AuthenticationType.BASIC;
import static com.pbs.middleware.server.common.security.AuthenticationType.JWT;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringfoxConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Value("${middleware.server.security.auth.type:BASIC}")
    private AuthenticationType authenticationType;

    @Bean
    public Docket authApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Authorization")
                .apiInfo(apiInfo())
                .securityContexts(Lists.newArrayList(securityContext()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/auth"))
                .build();
    }

    @Bean
    public Docket adminApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Users")
                .apiInfo(apiInfo())
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(singletonList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage(MiddlewareServer.class.getPackage().getName()))
                .paths(PathSelectors.ant(USERS))
                .build()
                .ignoredParameterTypes(Principal.class, Pageable.class, Sort.class, JsonNode.class)
                .directModelSubstitute(URL.class, String.class)
                .tags(new Tag(TagsConfig.USERS_TAG, "Users operations"));
    }

    @Bean
    public Docket templateApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Templates")
                .apiInfo(apiInfo())
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(singletonList(apiKey()))
                .select()
                .apis(and(
                        RequestHandlerSelectors.basePackage(TemplateController.class.getPackage().getName()),
                        not(RequestHandlerSelectors.withClassAnnotation(ApiIgnore.class))
                ))
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(Principal.class, Pageable.class, Sort.class, JsonNode.class)
                .directModelSubstitute(URL.class, String.class)
                .tags(
                        new Tag(TagsConfig.TEMPLATES_TAG, "Templates operations")
                );
    }

    @Bean
    public Docket jobsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Jobs")
                .apiInfo(apiInfo())
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(singletonList(apiKey()))
                .select()
                .apis(and(
                        RequestHandlerSelectors.basePackage(JobsController.class.getPackage().getName()),
                        not(RequestHandlerSelectors.withClassAnnotation(ApiIgnore.class))
                ))
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(Principal.class, Pageable.class, Sort.class, JsonNode.class)
                .directModelSubstitute(URL.class, String.class)
                .tags(
                        new Tag(TagsConfig.JOBS_TAG, "Jobs operations")
                );
    }

    @Bean
    public Docket connectionsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Connections")
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(singletonList(apiKey()))
                .select()
                .apis(and(RequestHandlerSelectors.basePackage(ConnectionsController.class.getPackage().getName()),
                        RequestHandlerSelectors.withClassAnnotation(Basic.class)))
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(Principal.class, Pageable.class, Sort.class, JsonNode.class)
                .directModelSubstitute(URL.class, String.class)
                .tags(new Tag(TagsConfig.CONNECTIONS_TAG, "Connections operations"));
    }

    @Bean
    public Docket scriptsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Scripts")
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(singletonList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage(ScriptController.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(Principal.class, Pageable.class, Sort.class, JsonNode.class)
                .directModelSubstitute(URL.class, String.class)
                .tags(new Tag(TagsConfig.SCRIPTS_TAG, "Scripts operations"));
    }

    @Bean
    public Docket contactsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Contacts")
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(singletonList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage(ContactController.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(Principal.class, Pageable.class, Sort.class, JsonNode.class)
                .directModelSubstitute(URL.class, String.class)
                .tags(new Tag(TagsConfig.CONTACTS_TAG, "Contacts operations"));
    }

    @Bean
    public Docket uploadApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Uploads")
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(singletonList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage(UploadController.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(Principal.class, Pageable.class, Sort.class, JsonNode.class)
                .directModelSubstitute(URL.class, String.class)
                .tags(new Tag(TagsConfig.UPLOADS_TAG, "Upload operations"));
    }

    @Bean
    public Docket downloadApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Downloads")
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(singletonList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage(DownloadController.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(Principal.class, Pageable.class, Sort.class, JsonNode.class)
                .directModelSubstitute(URL.class, String.class)
                .tags(new Tag(TagsConfig.DOWNLOADS_TAG, "Download operations"));
    }


    @Bean
    public Docket connectionsAdvancedApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Connections - advanced")
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(singletonList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage(ConnectionsController.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(Principal.class, Pageable.class, Sort.class, JsonNode.class)
                .directModelSubstitute(URL.class, String.class)
                .tags(
                        new Tag(TagsConfig.CONNECTIONS_TAG, "Connections advanced API operations")
                );
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors
                        .regex("^(" + String.join("|", TEMPLATES,  USERS, JOB_PREFIX, CONNECTIONS, SCRIPTS, CONTACTS, UPLOADS_PREFIX, DOWNLOADS_PREFIX) + ").*$"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        final String reference = authenticationType == JWT ? "JWT" : "basicAuth";
        return Lists.newArrayList(
                new SecurityReference(reference, authorizationScopes));
    }

    @Bean
    SecurityScheme apiKey() {
        if (authenticationType == JWT) {
            return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
        }
        if (authenticationType == BASIC) {
            return new BasicAuth("basicAuth");
        }
        throw new IllegalStateException("Invalid AUTH type provided: " + authenticationType);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("LL Middleware REST API documentation")
                .description("This pages contain LL Middleware REST documentation.")
                .version("1.0")
                .contact(new Contact("Kamil Triščík", null, "kamil.triscik@gmail.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .termsOfServiceUrl("TBD")
                .extensions(emptyList())
                .build();
    }


    //    @Bean
    public ApiListingScannerPlugin dynamicApis() {
        return new ApiListingScannerPlugin() {

            @Override
            public List<ApiDescription> apply(DocumentationContext documentationContext) {
                return new ArrayList<>(
                        singletonList(
                                new ApiDescription(
                                        "AUTH GR",
                                        "/auth",
                                        "Login and get JWT token",
                                        Arrays.asList(
                                                new OperationBuilder(new CachingOperationNameGenerator())
                                                        .authorizations(new ArrayList())
                                                        .codegenMethodNameStem("login")
                                                        .method(HttpMethod.POST)
                                                        .parameters(
                                                                Arrays.asList(
                                                                        new ParameterBuilder()
                                                                                .description("Login parameter")
                                                                                .type(new TypeResolver().resolve(AccountCredentials.class))
                                                                                .name("email")
                                                                                .parameterType("body")
                                                                                .parameterAccess("access")
                                                                                .required(true)
                                                                                .modelRef(new ModelRef("AccountCredentials"))
                                                                                .build()
                                                                )
                                                        )
                                                        .build()
                                        ),
                                        false
                                )
                        )
                );
            }

            @Override
            public boolean supports(DocumentationType documentationType) {
                return DocumentationType.SWAGGER_2.equals(documentationType);
            }
        };
    }
}
