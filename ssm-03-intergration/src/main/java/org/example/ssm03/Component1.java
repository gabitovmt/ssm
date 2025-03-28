package org.example.ssm03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.annotation.OnEventNotAccepted;
import org.springframework.statemachine.annotation.OnStateChanged;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Service;

@Service
@WithStateMachine(name = StateMachines.SM1)
@Slf4j
public class Component1 {

    @OnTransition(source = "S1", target = "S2")
    public void fromS1ToS2() {
        log.info("fromS1ToS2");
    }

    @StatesOnTransition(source = States.S0, target = States.S1)
    public void fromS0ToS1() {
        log.info("fromS0ToS1");
    }

    @OnStateChanged
    public void anyStateChange() {
        log.info("anyStateChange");
    }

    @OnStateChanged(source = "S0", target = "S1")
    public void stateChangeFromS0ToS1() {
        log.info("stateChangeFromS0ToS1");
    }

    @StatesOnStates(source = States.S1, target = States.S2)
    public void stateChangeFromS1ToS2() {
        log.info("stateChangeFromS1ToS2");
    }

    @OnEventNotAccepted
    public void anyEventNotAccepted() {
        log.info("anyEventNotAccepted");
    }

    @OnEventNotAccepted(event = "A")
    public void aEventNotAccepted() {
        log.info("aEventNotAccepted");
    }
}
