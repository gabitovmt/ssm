package org.example.ssm59.actions;

import org.example.ssm59.domain.*;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import reactor.core.publisher.Mono;

import java.time.LocalTime;

public class PlayingAction implements Action<States, Events> {

    @Override
    public void execute(StateContext<States, Events> context) {
        var extState = context.getExtendedState();
        LocalTime elapsed = extState.get(Variables.ELAPSED_TIME, LocalTime.class);
        Track track = extState.get(Variables.TRACK, Track.class);
        if (elapsed == null) {
            return;
        }

        LocalTime e = elapsed.plusSeconds(1);
        if (e.isAfter(track.length())) {
            var message = MessageBuilder
                    .withPayload(Events.FORWARD)
                    .setHeader(Headers.TRACKSHIFT.name(), 1)
                    .build();
            context.getStateMachine()
                    .sendEvent(Mono.just(message))
                    .subscribe();
        } else {
            extState.getVariables().put(Variables.ELAPSED_TIME, e);
        }
    }
}
