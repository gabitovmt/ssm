package org.example.ssm53;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.statemachine.event.OnStateEntryEvent;
import org.springframework.statemachine.event.OnStateExitEvent;
import org.springframework.statemachine.event.OnTransitionEvent;
import org.springframework.statemachine.event.StateMachineEvent;
import org.springframework.statemachine.transition.TransitionKind;

@Slf4j
public class EventListener implements ApplicationListener<StateMachineEvent> {

    @Override
    public void onApplicationEvent(StateMachineEvent event) {
        if (event instanceof OnStateEntryEvent e) {
            log.info("Entry state {}", e.getState().getId());
        } else if (event instanceof OnStateExitEvent e) {
            log.info("Exit state {}", e.getState().getId());
        } else if (event instanceof OnTransitionEvent e && e.getTransition().getKind() == TransitionKind.INTERNAL) {
            log.info("Internal transition source={}", e.getTransition().getSource().getId());
        }
    }
}
