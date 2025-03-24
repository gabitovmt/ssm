package org.example.ssm55.command;

import lombok.RequiredArgsConstructor;
import org.example.ssm55.domain.Event;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.statemachine.StateMachine;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Command
@RequiredArgsConstructor
public class StateMachineCommands {
    private final StateMachine<String, String> stateMachine;

    @Command(command = "sm state", description = "Напечатать текущее состояние")
    public String state() {
        var state = stateMachine.getState();
        if (state != null) {
            return StringUtils.collectionToCommaDelimitedString(state.getIds());
        } else {
            return "No state";
        }
    }

    @Command(command = "sm start", description = "Запуск конечного автомата")
    public String start() {
        stateMachine.startReactively().subscribe();
        return "State machine started";
    }

    @Command(command = "sm stop", description = "Остановка конечного автомата")
    public String stop() {
        stateMachine.stopReactively().subscribe();
        return "State machine stopped";
    }

    @Command(command = "sm event", description = "Отправка события в конечный автомат")
    public String event(@Option(longNames = {"", "event"}, required = true) Event event) {
        stateMachine.sendEvent(
                Mono.just(MessageBuilder.withPayload(event.toString()).build())
        ).subscribe();

        return "Event " + event + " send";
    }
}
