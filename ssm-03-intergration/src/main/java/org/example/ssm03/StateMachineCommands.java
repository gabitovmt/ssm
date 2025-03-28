package org.example.ssm03;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.statemachine.StateMachine;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Command
public class StateMachineCommands {
    private final Map<String, StateMachine<?, ?>> stateMachines = new HashMap<>();

    public StateMachineCommands(
            @Qualifier(StateMachines.SM0) StateMachine<String, String> stateMachine,
            @Qualifier(StateMachines.SM1) StateMachine<States, Events> stateMachine1
    ) {
        stateMachines.put(StateMachines.SM0, stateMachine);
        stateMachines.put(StateMachines.SM1, stateMachine1);
    }

    private StateMachine<?, ?> getStateMachine(String id) {
        return stateMachines.get(id);
    }

    @Command(command = "sm start", description = "Запуск конечного автомата")
    public String start(
            @Option(required = true, defaultValue = StateMachines.SM0, description = "Наименование конечного автомата")
            String machineId
    ) {
        getStateMachine(machineId).startReactively().subscribe();
        return "State machine started";
    }

    @Command(command = "sm stop", description = "Остановка конечного автомата")
    public String stop(
            @Option(required = true, defaultValue = StateMachines.SM0, description = "Наименование конечного автомата")
            String machineId
    ) {
        getStateMachine(machineId).stopReactively().subscribe();
        return "State machine stopped";
    }

    @Command(command = "sm state", description = "Напечатать текущее состояние")
    public String state(
            @Option(required = true, defaultValue = StateMachines.SM0, description = "Наименование конечного автомата")
            String machineId
    ) {
        var state = getStateMachine(machineId).getState();
        if (state != null) {
            return StringUtils.collectionToCommaDelimitedString(state.getIds());
        } else {
            return "No state";
        }
    }

    @SuppressWarnings("unchecked")
    @Command(command = "sm event", description = "Отправить событие в конечный автомат")
    public String event(
            @Option(required = true, description = "Событие") String event,
            @Option(required = true, defaultValue = StateMachines.SM0, description = "Наименование конечного автомата")
            String machineId
    ) {
        switch (machineId) {
            case StateMachines.SM0:
                sendEvent((StateMachine<String, String>) getStateMachine(machineId), event);
                break;
            case StateMachines.SM1:
                sendEvent((StateMachine<States, Events>) getStateMachine(machineId), Events.valueOf(event));
                break;
            default:
                // ignore
        }

        return "Event " + event + " send";
    }

    private <S, E> void sendEvent(StateMachine<S, E> stateMachine, E event) {
        stateMachine
                .sendEvent(Mono.just(MessageBuilder.withPayload(event).build()))
                .subscribe();
    }

    @Command(command = "sm extstate", description = "Изменение значения в расширенном состоянии конечного автомата")
    public void changeExtendedState(
            @Option(required = true, description = "Ключ") String key,
            @Option(required = true, description = "Значение") String value,
            @Option(required = true, defaultValue = StateMachines.SM0, description = "Наименование конечного автомата")
            String machineId
    ) {
        getStateMachine(machineId).getExtendedState().getVariables().put(key, value);
    }
}
