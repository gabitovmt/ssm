package org.example.ssm03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.EventHeader;
import org.springframework.statemachine.annotation.EventHeaders;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@WithStateMachine(name = StateMachines.SM0)
@Slf4j
public class Component {

    @OnTransition
    public void anyTransition() {
        log.info("TRANSITION");
    }

    @OnTransition
    public void transitionA(StateContext<String, String> context) {
        log.info("TRANSITION sample A: {}", context);
    }

    @OnTransition
    public void transitionB(
            @EventHeaders Map<String, Object> headers,
            @EventHeader("timestamp") Object timestamp,
            @EventHeader(name = "id", required = false) Object id,
            ExtendedState extendedState,
            StateMachine<String, String> stateMachine,
            Message<String> message,
            Exception e) {
        log.info("TRANSITION sample B: headers {}, timestamp {}, id {}, extendedState {}, stateMachine {}, message {}, e {}",
                headers, timestamp, id, extendedState, stateMachine, message, e);
    }
}
