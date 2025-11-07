package com.backend.recMeuble;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = {
                "com.cwa.springboot_app",       // configuration, contrôleurs, etc.
                "com.backend.recMeuble"         // entity, repository, service, config JWT
        }
)
@EnableJpaRepositories(basePackages = "com.backend.recMeuble.repository")
@EntityScan(basePackages = "com.backend.recMeuble.entity")
public class RecMeubleApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecMeubleApplication.class, args);
    }
}
