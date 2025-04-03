package org.example.ssm59.actions;

import org.example.ssm59.domain.*;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import reactor.core.publisher.Mono;

import java.time.LocalTime;

public class TrackAction implements Action<States, Events> {

    @Override
    public void execute(StateContext<States, Events> context) {
        var extState = context.getExtendedState();
        Integer trackShift = context.getMessageHeaders().get(Headers.TRACKSHIFT.name(), Integer.class);
        Track track = extState.get(Variables.TRACK, Track.class);
        Cd cd = extState.get(Variables.CD, Cd.class);

        if (trackShift == null || track == null || cd == null) {
            return;
        }

        int trackIndex = cd.tracks().indexOf(track);
        int next = trackIndex + trackShift;

        if (next >= 0 && cd.tracks().size() > next) {
            extState.getVariables().put(Variables.ELAPSED_TIME, LocalTime.of(0, 0));
            extState.getVariables().put(Variables.TRACK, cd.tracks().get(next));
        } else if (cd.tracks().size() <= next) {
            var message = MessageBuilder
                    .withPayload(Events.STOP)
                    .build();

            context.getStateMachine()
                    .sendEvent(Mono.just(message))
                    .subscribe();
        }
    }
}
