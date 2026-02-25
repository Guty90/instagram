package com.instagram.api_gateway.config;

import com.instagram.api_gateway.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public RouterFunction<ServerResponse> authRoute() {
        return GatewayRouterFunctions.route("auth-service")
                .route(path("/auth/**"), HandlerFunctions.http("http://localhost:8085"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> likeRoute() {
        return GatewayRouterFunctions.route("like-service")
                .route(path("/api/likes/**"), HandlerFunctions.http("http://localhost:8081"))
                .filter(jwtAuthFilter.apply())
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> notificationRoute() {
        return GatewayRouterFunctions.route("notification-service")
                .route(path("/api/notifications/**"), HandlerFunctions.http("http://localhost:8082"))
                .filter(jwtAuthFilter.apply())
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userRoute() {
        return GatewayRouterFunctions.route("user-service")
                .route(path("/api/users/**"), HandlerFunctions.http("http://localhost:8083"))
                .filter(jwtAuthFilter.apply())
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> storyRoute() {
        return GatewayRouterFunctions.route("story-service")
                .route(path("/api/stories/**"), HandlerFunctions.http("http://localhost:8084"))
                .filter(jwtAuthFilter.apply())
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> notificationWsRoute() {
        return GatewayRouterFunctions.route("notification-ws")
                .route(path("/ws/**"), HandlerFunctions.http("http://localhost:8082"))
                .build();
    }
}