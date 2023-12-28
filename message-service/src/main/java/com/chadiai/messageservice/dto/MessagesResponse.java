package com.chadiai.messageservice.dto;

import com.chadiai.messageservice.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessagesResponse {
    private String id;
    private List<Message> messages;
    private List<Integer> participants;
}
