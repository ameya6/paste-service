package org.data.model.request;

import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Data
public class PasteRequest {
    private Long userId;
    private UUID userUUID;
    private Long expiryInSeconds;
    private String title;
    private String data;

    public PasteRequest(){}
}
