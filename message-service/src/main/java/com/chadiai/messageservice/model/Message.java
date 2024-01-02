package com.chadiai.messageservice.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private Date timestamp;
    private int senderId;
    private int receiverId;
    private String content;
    @Builder.Default
    private boolean seen = false;
}
