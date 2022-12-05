package org.data.model.response;

import lombok.*;

@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class DUIDResponse {
    private Long duid;
    private String message;

    public DUIDResponse() {}
}
