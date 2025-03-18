package org.example.ssm53;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Command
@RequiredArgsConstructor
public class StateMachineCommands {
    private final StateMachine<States, Events> stateMachine;

    @Command(command = "sm state", description = "Напечатать текущее состояние")
    public String state() {
        State<States, Events> state = stateMachine.getState();
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

    @Command(command = "sm event", description = "Отправить событие в конечный автомат")
    public String event(
            @Option(longNames = { "", "event" }, required = true, description = "The event") Events event
    ) {
        stateMachine
                .sendEvent(Mono.just(MessageBuilder.withPayload(event).build()))
                .subscribe();

        return "Event " + event + " send";
    }

    @Command(command = "sm variables", description = "Напечатать переменные расширенного состояния")
    public String variables() {
        var variablesEntrySet = stateMachine.getExtendedState().getVariables().entrySet();
        if (variablesEntrySet.isEmpty()) {
            return "No variables";
        }

        return variablesEntrySet.stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("\n"));
    }
}
