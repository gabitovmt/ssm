package org.example.ssm03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

@Component
@WithStateMachine
@Slf4j
public class Comp {

    @OnTransition
    public void anyTransition() {
        log.info("TRANSITION");
    }
}
