package org.example.ssm51;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.util.StringUtils;

@Command
@RequiredArgsConstructor
public class AbstractStateMachineCommands<S, E> {

    @Getter(value = AccessLevel.PROTECTED)
    private final StateMachine<S, E> stateMachine;

    @Command(command = "sm state", description = "Напечатать текущее состояние")
    public String state() {
        State<S, E> state = stateMachine.getState();
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
}
