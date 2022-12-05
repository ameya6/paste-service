package org;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@Log4j2
@SpringBootApplication
@ComponentScan({"org.paste", "org.data"})
public class PasteBinMain {
    public static void main(String[] args) {
        SpringApplication.run(PasteBinMain.class, args);
    }
}
