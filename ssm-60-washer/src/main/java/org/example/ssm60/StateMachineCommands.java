package org.example.ssm60;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.statemachine.StateMachine;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Command
public class StateMachineCommands {
    private final StateMachine<States, Events> stateMachine;
    private final String stateChartModel;

    public StateMachineCommands(
            StateMachine<States, Events> stateMachine,
            @Qualifier("stateChartModel") String stateChartModel
    ) {
        this.stateMachine = stateMachine;
        this.stateChartModel = stateChartModel;
    }

    @Command(command = "sm print", description = "Напечатать конечный автомат")
    public String print() {
        return stateChartModel;
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

    @Command(command = "sm state", description = "Напечатать текущее состояние")
    public String state() {
        var state = stateMachine.getState();
        if (state != null) {
            return StringUtils.collectionToCommaDelimitedString(state.getIds());
        } else {
            return "No state";
        }
    }

    @Command(command = "sm event", description = "Отправить событие в конечный автомат")
    public String event(
            @Option(required = true, description = "Событие") Events event
    ) {
        stateMachine.sendEvent(
                Mono.just(MessageBuilder.withPayload(event).build())
        ).subscribe();

        return "Event " + event + " send";
    }
}
