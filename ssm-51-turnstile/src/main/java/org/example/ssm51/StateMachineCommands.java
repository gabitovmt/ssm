package org.example.ssm51;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;

@Command
public class StateMachineCommands extends AbstractStateMachineCommands<States, Events> {

    public StateMachineCommands(StateMachine<States, Events> stateMachine) {
        super(stateMachine);
    }

    @Command(command = "sm event", description = "Отправить событие в конечный автомат")
    public String event(@Option(longNames = { "", "event" }, required = true, description = "Событие") Events event) {
        getStateMachine()
                .sendEvent(Mono.just(MessageBuilder.withPayload(event).build()))
                .subscribe();

        return "Event " + event + " send";
    }
}
