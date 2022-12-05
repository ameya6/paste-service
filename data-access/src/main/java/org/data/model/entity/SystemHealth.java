package org.data.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class SystemHealth {
    private String message;

    public SystemHealth(){}
}
