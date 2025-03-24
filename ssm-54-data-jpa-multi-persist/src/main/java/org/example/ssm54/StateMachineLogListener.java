package org.example.ssm54;

import lombok.Getter;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

import java.util.LinkedList;

@Getter
public class StateMachineLogListener extends StateMachineListenerAdapter<String, String> {
    private final LinkedList<String> messages = new LinkedList<>();

    public void resetMessages() {
        messages.clear();
    }

    @Override
    public void stateContext(StateContext<String, String> stateContext) {
        switch (stateContext.getStage()) {
            case STATE_ENTRY -> messages.addFirst("Enter " + stateContext.getTarget().getId());
            case STATE_EXIT -> messages.addFirst("Exit " + stateContext.getSource().getId());
            case STATEMACHINE_START -> messages.addFirst("Machine started");
            case STATEMACHINE_STOP -> messages.addFirst("Machine stopped");
            default -> {
                // ignore
            }
        }
    }
}
