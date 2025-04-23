package org.example.moderatorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ModeratorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModeratorServiceApplication.class, args);
    }

}
