package com.chadiai.messageservice.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(value = "message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private String id;
    private Date timestamp;
    private int senderId;
    private String content;
    @Builder.Default
    private boolean seen = false;
}
