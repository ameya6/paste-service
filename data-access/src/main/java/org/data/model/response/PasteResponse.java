package org.data.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class PasteResponse {
    private String url;
    private String message;
    private String preview;
    private StringBuilder data;

    public PasteResponse() {}
}
