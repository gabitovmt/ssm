package org.example.ssm59.cdplayer;

import lombok.RequiredArgsConstructor;
import org.example.ssm59.domain.*;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.WithStateMachine;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@WithStateMachine
@RequiredArgsConstructor
public class CdPlayer {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("mm:ss");
    private final StateMachine<States, Events> stateMachine;
    private String cdStatus = "No CD";
    private String trackStatus = "";

    public void load(Cd cd) {
        var message = MessageBuilder
                .withPayload(Events.LOAD)
                .setHeader(Variables.CD.name(), cd)
                .build();

        stateMachine
                .sendEvent(Mono.just(message))
                .subscribe();
    }

    public void play() {
        var message = MessageBuilder
                .withPayload(Events.PLAY)
                .build();

        stateMachine
                .sendEvent(Mono.just(message))
                .subscribe();
    }

    public void stop() {
        var message = MessageBuilder
                .withPayload(Events.STOP)
                .build();

        stateMachine
                .sendEvent(Mono.just(message))
                .subscribe();
    }

    public void pause() {
        var message = MessageBuilder
                .withPayload(Events.PAUSE)
                .build();

        stateMachine
                .sendEvent(Mono.just(message))
                .subscribe();
    }

    public void eject() {
        var message = MessageBuilder
                .withPayload(Events.EJECT)
                .build();

        stateMachine
                .sendEvent(Mono.just(message))
                .subscribe();
    }

    public void forward() {
        var message = MessageBuilder
                .withPayload(Events.FORWARD)
                .setHeader(Headers.TRACKSHIFT.name(), 1)
                .build();

        stateMachine
                .sendEvent(Mono.just(message))
                .subscribe();
    }

    public void back() {
        var message = MessageBuilder
                .withPayload(Events.BACK)
                .setHeader(Headers.TRACKSHIFT.name(), -1)
                .build();

        stateMachine
                .sendEvent(Mono.just(message))
                .subscribe();
    }

    public String getLdcStatus() {
        return cdStatus + ' ' + trackStatus;
    }

    @StatesOnTransition(target = States.BUSY)
    public void busy(ExtendedState extendedState) {
        Cd cd = extendedState.get(Variables.CD, Cd.class);
        if (cd != null) {
            cdStatus = cd.name();
        }
    }

    @StatesOnTransition(target = States.PLAYING)
    public void playing(ExtendedState extendedState) {
        LocalTime elapsed = extendedState.get(Variables.ELAPSED_TIME, LocalTime.class);
        Track track = extendedState.get(Variables.TRACK, Track.class);
        if (elapsed != null && track != null) {
            trackStatus = track + " " + elapsed.format(FORMATTER);
        }
    }

    @StatesOnTransition(target = States.OPEN)
    public void open() {
        cdStatus = "Open";
    }

    @StatesOnTransition(target = {States.CLOSED, States.IDLE})
    public void closed(ExtendedState extendedState) {
        Cd cd = extendedState.get(Variables.CD, Cd.class);
        cdStatus = cd != null ? cd.name() : "No CD";
        trackStatus = "";
    }
}
