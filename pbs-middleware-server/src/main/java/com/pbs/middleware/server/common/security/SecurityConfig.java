package com.pbs.middleware.server.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbs.middleware.server.common.security.jwt.JWTAuthenticationFilter;
import com.pbs.middleware.server.common.security.jwt.JWTLoginFilter;
import com.pbs.middleware.server.common.security.jwt.TokenAuthenticationService;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.pbs.middleware.server.common.security.AuthenticationType.BASIC;
import static com.pbs.middleware.server.common.security.AuthenticationType.JWT;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.HEAD;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties({
        SecurityProperties.class,
        DefaultAdminProperties.class,
        UrlProperties.class,
        JwtProperties.class
})
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Allow any user.
     */
    public static final String ALLOW_ALL = "permitAll";

    /**
     * Allow administrator user only.
     */
    public static final String ALLOW_ADMIN = "hasRole('ROLE_ADMIN')";

    /**
     * Allow customer user only.
     */
    private static final String ALLOW_CUSTOMER = "hasRole('ROLE_CUSTOMER')";

    /**
     * Allow any authenticated user.
     */
    public static final String ALLOW_AUTHENTICATED = "isAuthenticated()";

    @Value("${middleware.server.security.auth.type:BASIC}")
    private AuthenticationType authenticationType;

    @NonNull
    private final TokenAuthenticationService tokenAuthenticationService;

    @PostConstruct
    public void init() {
        log.info("Authentication type: {}", authenticationType);
            SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public BasicAuthenticationEntryPoint getBasicAuthenticationEntryPoint(){
        BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = new BasicAuthenticationEntryPoint();
        basicAuthenticationEntryPoint.setRealmName("Basic Authentication");
        return basicAuthenticationEntryPoint;
    }


    @Bean
    public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter(
            SecurityUserDetailsService userDetailsService, ObjectMapper objectMapper, UrlProperties urlProperties, SecurityProperties properties) {

        return new WebSecurityConfigurerAdapter() {
            @Override
            protected void configure(AuthenticationManagerBuilder auth) {
                auth.authenticationProvider(authenticationProvider(userDetailsService));
            }

            @Override
            protected void configure(HttpSecurity http) throws Exception {
                HttpSecurity security = http.csrf().disable().authorizeRequests()
                        .antMatchers(POST, urlProperties.getAuth()).permitAll()
                        .antMatchers(POST, urlProperties.getLogout()).permitAll()
                        .antMatchers(properties.getSwagger().split(";")).permitAll()
                        .antMatchers("/actuator/**").permitAll()
                        .antMatchers("/docs/**").permitAll()
                        .antMatchers("/").permitAll()
//                        .antMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                        .and();
                if (authenticationType == JWT) {
                    security.addFilterBefore(
                            new JWTLoginFilter(
                                    urlProperties.getAuth(),
                                    authenticationManager(),
                                    objectMapper,
                                    tokenAuthenticationService),
                            UsernamePasswordAuthenticationFilter.class
                    )
                            .addFilterBefore(
                                    new JWTAuthenticationFilter(tokenAuthenticationService),
                                    UsernamePasswordAuthenticationFilter.class);
                }
                if (authenticationType == BASIC) {
                    security.httpBasic()
                            .authenticationEntryPoint(getBasicAuthenticationEntryPoint());
                    http.addFilterAfter(new BasicAuthenticationFilter(authenticationManager()),
                            BasicAuthenticationFilter.class);
                }

            }
        };
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(SecurityUserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    @ConditionalOnProperty("middleware.server.security.cors")
    public WebMvcConfigurer corsConfigurer(SecurityProperties properties, List<MiddlewareInterceptor> interceptors) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                log.info("Configuring CORS Registry ...");
                log.debug("Allowed CORS origins: " + properties.getCors());
                registry.addMapping("/**")
                        .allowCredentials(true)
                        .allowedOrigins(properties.getCors().split(";"))
                        .allowedMethods(OPTIONS.name(), GET.name(), POST.name(), PUT.name(), DELETE.name(), PATCH.name(), HEAD.name())
                        .allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept");
            }

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addRedirectViewController("/docs/", "/docs/pmd.html");
                registry.addRedirectViewController("/", "/docs/pmd.html");
                registry.addRedirectViewController("", "/docs/pmd.html");
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/docs").addResourceLocations("classpath:/static/docs/site/index.html");
                registry.addResourceHandler("/docs/").addResourceLocations("classpath:/static/docs/site/index.html");
                registry.addResourceHandler("/docs/**").addResourceLocations("classpath:/static/docs/site/");
//                registry.addResourceHandler("/docs/css/**").addResourceLocations(new String[]{"classpath:/static/docs/site/css/"});
//                registry.addResourceHandler("/docs/images/**").addResourceLocations(new String[]{"classpath:/static/docs/images/"});
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                interceptors.forEach(interceptor ->
                    registry.addInterceptor(interceptor)
                            .addPathPatterns(interceptor.getPathPatterns())
                            .excludePathPatterns(interceptor.getExcludedPatterns())
                );
            }
        };
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnProperty("middleware.server.security.cors")
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(CorsConfiguration.ALL);
        config.setAllowedMethods(Stream.of(GET.name(), POST.name(), PUT.name(), DELETE.name(), OPTIONS.name(), PATCH.name()).collect(toList()));
        config.setMaxAge(3600L);
        config.setAllowedHeaders(Stream.of("Authorization", "content-type", "xsrf-token", "Access-Token").collect(toList()));
        config.addExposedHeader("xsrf-token");
        config.addExposedHeader("Authorization");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
