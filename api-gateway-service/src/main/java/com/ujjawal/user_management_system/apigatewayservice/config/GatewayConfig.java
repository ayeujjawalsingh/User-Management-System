package com.ujjawal.user_management_system.apigatewayservice.config;

import com.ujjawal.user_management_system.apigatewayservice.filters.AuthFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import org.springframework.stereotype.Component;

@Component
public class GatewayConfig {

    private final AuthFilter authFilter;

    public GatewayConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/user/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("http://localhost:9090"))
                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("http://localhost:9092"))
                .route("password-service", r -> r.path("/password/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("http://localhost:9096"))
                .route("otp-service", r -> r.path("/otp/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("http://localhost:9094"))
                .route("notification-service", r -> r.path("/notification/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthFilter.Config())))
                        .uri("http://localhost:9098"))
                .build();
    }
}
