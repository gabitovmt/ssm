package org.example.ssm02;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

@AllArgsConstructor
@SpringBootApplication
public class Application implements CommandLineRunner {

    private final StateMachine<String, String> stateMachine;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        stateMachine.sendEvent(Events.DEPLOY);
        stateMachine.sendEvent(Events.DEPLOY);
        Thread.sleep(10000);
    }
}
