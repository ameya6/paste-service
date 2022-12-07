package org.data.model.params;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@ToString
@AllArgsConstructor
@Getter
@Setter
@Configuration
@NoArgsConstructor
@EnableConfigurationProperties
@ConfigurationProperties("cache")
public class CacheParams {
    private String url;
    private Integer port;
}
