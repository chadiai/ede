package fact.it.messageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {
    private String id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private boolean seen;
}