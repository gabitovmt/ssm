package org.example.ssm02;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.Optional;

@Slf4j
@Configuration
@EnableStateMachine
public class StateMachineConfig extends StateMachineConfigurerAdapter<String, String> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        config.withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    @Bean
    public StateMachineListener<String, String> listener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<String, String> from, State<String, String> to) {
                log.info("{} State change from '{}' to '{}'",
                        System.currentTimeMillis(),
                        Optional.ofNullable(from).map(State::getId).orElse("null"),
                        to.getId());
            }
        };
    }

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states.withStates()
                .initial(States.READY)
                .state(States.DEPLOY_PREPARE, Events.DEPLOY)
                .state(States.DEPLOY_EXECUTE, Events.DEPLOY);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
        transitions
                .withExternal().source(States.READY).target(States.DEPLOY_PREPARE).event(Events.DEPLOY)
                .and()
                .withExternal().source(States.DEPLOY_PREPARE).target(States.DEPLOY_EXECUTE)
                .and()
                .withExternal().source(States.DEPLOY_EXECUTE).target(States.READY);
    }
}
