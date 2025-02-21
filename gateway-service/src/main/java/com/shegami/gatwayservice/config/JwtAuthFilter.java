package com.shegami.gatwayservice.config;


import com.nimbusds.jose.shaded.gson.JsonObject;
import com.shegami.gatewayservice.UserResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static com.shegami.gatwayservice.utils.request.RequestHandler.resolveToken;
import static com.shegami.gatwayservice.utils.request.RequestHandler.writeResponse;
import static org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties.UiService.LOGGER;


@Component
public class JwtAuthFilter implements WebFilter {

    @Autowired
    private ReactiveJwtDecoder jwtDecoder;
    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {


        ServerHttpRequest request = exchange.getRequest();

        String requestedURI = request.getURI().getPath();

        if (requestedURI.contains("/auth-service/") || requestedURI.contains("/v3/api-docs") || requestedURI.contains("/swagger-ui/") || requestedURI.contains("api/user/")) {
            return chain.filter(exchange);
        }

        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return Mono.error(new RuntimeException("Missing Authorization Header"));
        }

        String requestToken = exchange.getRequest().getHeaders().getFirst("AUTHORIZATION");


        if (requestToken == null || !requestToken.startsWith("Bearer ")) {
            return Mono.error(new RuntimeException("Invalid Token"));
        }

        String token = requestToken.substring(7);

        Jwt jwt = jwtDecoder.decode(token).block();

        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(jwt.getSubject());

            List<SimpleGrantedAuthority> authorities = userDetails.getAuthorities().stream()
                    .map((role) -> new SimpleGrantedAuthority("ROLE_" + role)).toList();

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, authorities);

            SecurityContext context = new SecurityContextImpl(authenticationToken);

            var claims = jwt.getClaims();


            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("userId", (String) claims.get("userId"))
                    .header("email", (String) claims.get("email"))
                    .header("roles", claims.get("roles").toString())
                    .build();

            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)
                    .build();

            return chain.filter(modifiedExchange).contextWrite(
                    ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
        }
        return chain.filter(exchange);
    }
}