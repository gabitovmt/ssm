package org.example.ssm03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.annotation.*;
import org.springframework.stereotype.Service;

@Service
@WithStateMachine
@Slf4j
public class Component2 {

    @OnStateMachineStart
    public void onStateMachineStart() {
        log.info("onStateMachineStart");
    }

    @OnStateMachineStop
    public void onStateMachineStop() {
        log.info("onStateMachineStop");
    }

    @OnStateMachineError
    public void onStateMachineError() {
        log.error("onStateMachineError");
    }

    @OnExtendedStateChanged
    public void anyStateChange() {
        log.info("anyStateChange");
    }

    @OnExtendedStateChanged(key = "key1")
    public void key1Changed() {
        log.info("key1Changed");
    }
}
