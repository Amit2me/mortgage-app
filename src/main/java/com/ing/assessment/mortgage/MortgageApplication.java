package com.ing.assessment.mortgage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ING Mortgage Backend Application.
 *
 * This is the main entry point for the Spring Boot application. It bootstraps
 * the embedded web server and initializes the application context.
 */
@SpringBootApplication
public class MortgageApplication {
    public static void main(String[] args) {
        SpringApplication.run(MortgageApplication.class, args);
    }
}
