package fact.it.messageservice.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private String id;
    private Long senderId;
    private Long receiverId;
    private String content;
    @Builder.Default
    private boolean seen = false;
}
