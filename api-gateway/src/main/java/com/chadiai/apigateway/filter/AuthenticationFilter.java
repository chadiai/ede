package com.chadiai.apigateway.filter;

import com.chadiai.apigateway.service.EurekaService;
import com.chadiai.apigateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private RouteValidator validator;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EurekaService eurekaService;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return unauthorized(exchange, "Missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {
                    jwtUtil.validateToken(authHeader);
                } catch (Exception e) {
                    return unauthorized(exchange, "Unauthorized!");
                }
                String path = exchange.getRequest().getURI().getPath();

                // Delete appointment check: you can only delete your own appointment
                // path: appointment/{appointmentId}/delete
                Pattern appointmentDelete = Pattern.compile("/appointment/\\d+/delete");
                Matcher matcherDelete = appointmentDelete.matcher(path);
                if (matcherDelete.find()){
                    try {
                        String number = matcherDelete.group().replaceAll("/appointment/(\\d+)/delete", "$1");
                        String userEmailFromToken = jwtUtil.extractUser(authHeader);
                        int userId = eurekaService.fetchUserId(userEmailFromToken);
                        String auths = eurekaService.fetchAuthAppointments(userId);
                        List<String> authsList = Arrays.asList(auths.split(";")); // all the appointments you can delete
                        if (authsList.stream().noneMatch(number::contains)) {
                            return unauthorized(exchange, "Unauthorized access to appointment");
                        }
                    } catch (Exception e) {
                        return unauthorized(exchange, "Unauthorized access to appointment");
                    }
                }
                // View messages check: only accessible for participants
                // path: /messages/m/{messagesId}
                if (path.contains("/messages/m/")) {
                    try {
                        String userEmailFromToken = jwtUtil.extractUser(authHeader);
                        int userId = eurekaService.fetchUserId(userEmailFromToken);
                        String auths = eurekaService.fetchAuthMessages(userId);
                        List<String> authsList = Arrays.asList(auths.split(";"));
                        if (authsList.stream().noneMatch(path::contains)) {
                            return unauthorized(exchange, "Unauthorized access to message");
                        }
                    } catch (Exception e) {
                        return unauthorized(exchange, "Unauthorized access to message");
                    }
                }
            }
            return chain.filter(exchange);
        });
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(message.getBytes())));
    }

    public static class Config {

    }
}
