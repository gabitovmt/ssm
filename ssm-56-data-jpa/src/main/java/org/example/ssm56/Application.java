package org.example.ssm56;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.example.ssm56")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
