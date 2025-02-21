package com.shegami.gatwayservice.exceptions;


import com.nimbusds.jose.shaded.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

@ControllerAdvice
public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {



    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("Message","YOU DON'T HAVE ENOUGH PERMISSION" );
        // Get the response object
        ServerHttpResponse response = exchange.getResponse();

        // Set the HTTP status code to unauthorized
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        // Set the response body with the error message
        return response.setComplete();
    }
}