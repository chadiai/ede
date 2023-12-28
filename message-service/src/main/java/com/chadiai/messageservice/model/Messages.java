package com.chadiai.messageservice.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Messages {
    private String id;
    private List<Message> messages;
    private List<Integer> participants;
}
