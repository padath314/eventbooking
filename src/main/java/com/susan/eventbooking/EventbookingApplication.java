package com.susan.eventbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.susan.eventbooking")
public class EventbookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventbookingApplication.class, args);
    }
}