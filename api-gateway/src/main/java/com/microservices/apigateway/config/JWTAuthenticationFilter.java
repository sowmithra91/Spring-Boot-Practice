package com.microservices.apigateway.config;

import com.microservices.apigateway.services.JWTService;
import com.microservices.apigateway.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter /*extends OncePerRequestFilter */ implements WebFilter {

    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource;

    /*@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userId;

        if (StringUtils.isEmpty(authHeader) || !(authHeader.startsWith("Bearer "))) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userId = jwtService.extractUserName(jwt);

        if (StringUtils.isNotEmpty(userId) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userId);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                securityContext.setAuthentication(token);

                SecurityContextHolder.setContext(securityContext);
            }
        }
        filterChain.doFilter(request, response);
    }
     */

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String jwt = authHeader.substring(7);
        String userId = jwtService.extractUserName(jwt);

        if (StringUtils.hasText(userId) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userId);
             if(jwtService.isTokenValid(jwt, userDetails)) {
                 UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                         userDetails, null, userDetails.getAuthorities());
                 authenticationToken.setDetails(customWebAuthenticationDetailsSource.buildDetails(exchange.getRequest()));
                 SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                 securityContext.setAuthentication(authenticationToken);
                 Context context = ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext));
                 return chain.filter(exchange).contextWrite(context);
             }
        }
        return chain.filter(exchange);
    }
}
