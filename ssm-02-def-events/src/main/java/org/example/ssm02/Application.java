package org.example.ssm02;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@SpringBootApplication
public class Application implements CommandLineRunner {

    private final StateMachine<States, Events> stateMachine;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        stateMachine.sendEvents(Flux.just(deploy(), deploy())).subscribe();
    }

    private Message<Events> deploy() {
        return MessageBuilder.withPayload(Events.DEPLOY).build();
    }
}
