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
import org.springframework.statemachine.transition.Transition;

@Slf4j
@Configuration
@EnableStateMachine
public class StateMachineConfig extends StateMachineConfigurerAdapter<States, Events> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        config.withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    @Bean
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<States, Events> from, State<States, Events> to) {
                log.info("State change from '{}' to '{}'", stateId(from), stateId(to));
            }

            @Override
            public void transition(Transition<States, Events> tr) {
                log.info("Transition from '{}' to '{}'", stateId(tr.getSource()), stateId(tr.getTarget()));
            }

            private States stateId(State<States, Events> state) {
                return state != null ? state.getId() : null;
            }
        };
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states.withStates()
                .initial(States.READY)
                .state(States.DEPLOY_PREPARE, Events.DEPLOY)
                .state(States.DEPLOY_EXECUTE, Events.DEPLOY);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                .withExternal().source(States.READY).target(States.DEPLOY_PREPARE).event(Events.DEPLOY)
                .and()
                .withExternal().source(States.DEPLOY_PREPARE).target(States.DEPLOY_EXECUTE)
                .and()
                .withExternal().source(States.DEPLOY_EXECUTE).target(States.READY);
    }
}
