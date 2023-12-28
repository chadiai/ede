package com.chadiai.messageservice.client;

import com.chadiai.messageservice.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceClient {
    @Autowired
    private RestTemplate template;

    public UserResponse fetchUser(int id) {
        return template.getForObject("http://USER-SERVICE/user/" + id, UserResponse.class);
    }
}
