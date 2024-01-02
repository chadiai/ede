package com.chadiai.apigateway.service;

import com.chadiai.apigateway.dto.AuthResponse;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class EurekaService {
    private final EurekaClient eurekaClient;
    public String fetchAuthMessages(int userIdFromToken) {
        InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("MESSAGE-SERVICE", false);
        if (instanceInfo == null) {
            throw new RuntimeException("MESSAGE-SERVICE not found");
        }
        String baseUrl = instanceInfo.getHomePageUrl();
        String endpointUrl = baseUrl + "messages/auth/" + userIdFromToken;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(endpointUrl, String.class);
    }

    public String fetchAuthAppointments(int userIdFromToken) {
        InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("APPOINTMENT-SERVICE", false);
        if (instanceInfo == null) {
            throw new RuntimeException("APPOINTMENT-SERVICE not found");
        }
        String baseUrl = instanceInfo.getHomePageUrl();
        String endpointUrl = baseUrl + "appointment/auth/" + userIdFromToken;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(endpointUrl, String.class);
    }

    public AuthResponse fetchUserFromEmail(String userEmail) {
        InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("USER-SERVICE", false);
        if (instanceInfo == null) {
            throw new RuntimeException("USER-SERVICE not found");
        }
        String baseUrl = instanceInfo.getHomePageUrl();
        String endpointUrl = baseUrl + "user/email/" + userEmail;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(endpointUrl, AuthResponse.class);
    }
}
