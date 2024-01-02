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

import java.util.Objects;
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

                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {
                    jwtUtil.validateToken(authHeader);
                } catch (Exception e) {
                    return unauthorized(exchange, "Unauthorized!");
                }

                String path = exchange.getRequest().getURI().getPath();

                if (!handleAdmin(path, authHeader)) {
                    return unauthorized(exchange, "Unauthorized: not admin!");
                }

                if (!handleMessageSend(path, authHeader)) {
                    return unauthorized(exchange, "Unauthorized, wrong senderId");
                }

                if (!handleViewAppointments(path, authHeader)) {
                    return unauthorized(exchange, "Unauthorized, wrong initiator");
                }

                if (!handleAppointment(path, authHeader)) {
                    return unauthorized(exchange, "Unauthorized: user not initiator of appointment");
                }

                if (!handleViewMessages(path, authHeader)) {
                    return unauthorized(exchange, "Unauthorized access to message");
                }
            }
            return chain.filter(exchange);
        });
    }
    private boolean handleAdmin(String path, String authHeader) {
        List<String> adminEndpoints = List.of(
                "/user/",
                "/appointment/all",
                "/messages/all"
        );
        for (String endpoint : adminEndpoints) {
            if (path.contains(endpoint)) {
                String userEmailFromToken = jwtUtil.extractUser(authHeader);
                return eurekaService.fetchUserFromEmail(userEmailFromToken).isAdmin();
            }
        }
        return true;
    }

    private boolean checkRequester(String path, String authHeader, Pattern pattern) {
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            try {
                int requester = Integer.parseInt(matcher.group(1));
                String userEmailFromToken = jwtUtil.extractUser(authHeader);
                int userId = eurekaService.fetchUserFromEmail(userEmailFromToken).getUserId();
                return userId == requester;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    private boolean handleViewAppointments(String path, String authHeader) {
        Pattern pattern = Pattern.compile("/appointment/(\\d+)/all");
        return checkRequester(path, authHeader, pattern);
    }
    private boolean handleMessageSend(String path, String authHeader) {
        Pattern pattern = Pattern.compile("/messages/(\\d+)/send");
        return checkRequester(path, authHeader, pattern);
    }

    private boolean handleAppointment(String path, String authHeader) {
        Pattern pattern = Pattern.compile("/appointment/(\\d+)/(delete|edit)");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            try {
                String appointmentId = matcher.group(1);
                String userEmailFromToken = jwtUtil.extractUser(authHeader);
                int userId = eurekaService.fetchUserFromEmail(userEmailFromToken).getUserId();
                String auths = eurekaService.fetchAuthAppointments(userId);
                List<String> authsList = Arrays.asList(auths.split(";"));
                return authsList.contains(appointmentId);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    private boolean handleViewMessages(String path, String authHeader) {
        if (path.contains("/messages/m/")) {
            try {
                String userEmailFromToken = jwtUtil.extractUser(authHeader);
                int userId = eurekaService.fetchUserFromEmail(userEmailFromToken).getUserId();
                String auths = eurekaService.fetchAuthMessages(userId);
                List<String> authsList = Arrays.asList(auths.split(";"));
                return authsList.stream().anyMatch(path::contains);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(message.getBytes())));
    }

    public static class Config {

    }
}
