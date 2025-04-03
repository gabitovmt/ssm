package org.example.ssm60;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.configurers.StateConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

@Configuration
@EnableStateMachine
@Slf4j(topic = "washer")
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        config.withConfiguration()
                .listener(listener());
    }

    @Bean
    public StateMachineListener<States, Events> listener() {
        return new StateMachineListenerAdapter<>() {

            @Override
            public void stateEntered(State<States, Events> state) {
                log.info("Entry state {}", state.getId());
            }

            @Override
            public void stateExited(State<States, Events> state) {
                log.info("Exit state {}", state.getId());
            }
        };
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states
                .withStates()
                .initial(States.RUNNING)
                .state(States.POWER_OFF)
                .end(States.END)
                .and()

                .withStates()
                .parent(States.RUNNING)
                .initial(States.WASHING)
                .state(States.RINSING)
                .state(States.DRYING)
                .history(States.HISTORY, StateConfigurer.History.SHALLOW);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                .withExternal()
                .source(States.WASHING).target(States.RINSING)
                .event(Events.RINSE)
                .and()

                .withExternal()
                .source(States.RINSING).target(States.DRYING)
                .event(Events.DRY)
                .and()

                .withExternal()
                .source(States.RUNNING).target(States.POWER_OFF)
                .event(Events.CUT_POWER)
                .and()

                .withExternal()
                .source(States.POWER_OFF).target(States.HISTORY)
                .event(Events.RESTORE_POWER)
                .and()

                .withExternal()
                .source(States.RUNNING).target(States.END)
                .event(Events.STOP);
    }
}
