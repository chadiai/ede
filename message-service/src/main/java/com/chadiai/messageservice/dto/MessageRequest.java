package com.chadiai.messageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageRequest {
    private Date timestamp;
    private int senderId;
    private int receiverId;
    private String content;
}
