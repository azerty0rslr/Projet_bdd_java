package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        // Scan de toutes les pages, essai avec uniquement com.example mais ne fonctionne pas
        "com.example.app",
        "com.example.jpa",
        "com.example.mongo"
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}