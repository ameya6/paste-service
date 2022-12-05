package org.paste.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class PastebinConfig {
    @Bean
    public Gson gson() {
        return new GsonBuilder().create();
    }
}
