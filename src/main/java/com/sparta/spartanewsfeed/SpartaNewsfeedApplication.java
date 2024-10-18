package com.sparta.spartanewsfeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpartaNewsfeedApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpartaNewsfeedApplication.class, args);
    }

}
