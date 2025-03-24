package org.example.ssm55.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import static org.example.ssm55.domain.Event.*;
import static org.example.ssm55.domain.State.*;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends StateMachineConfigurerAdapter<String, String> {

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states.withStates()
                .initial(PLACED.toString())
                .state(PROCESSING.toString())
                .state(SENT.toString())
                .state(DELIVERED.toString());
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
        transitions.withExternal()
                .source(PLACED.toString()).target(PROCESSING.toString())
                .event(PROCESS.toString());
        transitions.withExternal()
                .source(PROCESSING.toString()).target(SENT.toString())
                .event(SEND.toString());
        transitions.withExternal()
                .source(SENT.toString()).target(DELIVERED.toString())
                .event(DELIVER.toString());
    }
}
