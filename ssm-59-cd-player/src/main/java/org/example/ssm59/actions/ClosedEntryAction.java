package org.example.ssm59.actions;

import org.example.ssm59.domain.Events;
import org.example.ssm59.domain.States;
import org.example.ssm59.domain.Variables;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import reactor.core.publisher.Mono;

public class ClosedEntryAction implements Action<States, Events> {

    @Override
    public void execute(StateContext<States, Events> context) {
        if (context.getTransition() != null
                && context.getEvent() == Events.PLAY
                && context.getTransition().getTarget().getId() == States.CLOSED
                && context.getExtendedState().getVariables().containsKey(Variables.CD)) {
            var message = MessageBuilder
                    .withPayload(Events.PLAY)
                    .build();

            context.getStateMachine()
                    .sendEvent(Mono.just(message))
                    .subscribe();
        }
    }
}
