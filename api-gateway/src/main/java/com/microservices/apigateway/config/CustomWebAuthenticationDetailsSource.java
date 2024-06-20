package com.microservices.apigateway.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

@Component
public class CustomWebAuthenticationDetailsSource implements AuthenticationDetailsSource<ServerHttpRequest, CustomWebAuthenticationDetailsSource> {
    @Override
    public CustomWebAuthenticationDetailsSource buildDetails(ServerHttpRequest context) {
        return new CustomWebAuthenticationDetailsSource().buildDetails(context);
    }
}
