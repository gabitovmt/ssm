package org.example.ssm59;

import lombok.extern.slf4j.Slf4j;
import org.example.ssm59.actions.*;
import org.example.ssm59.domain.Events;
import org.example.ssm59.domain.States;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

@Configuration
@EnableStateMachine
@Slf4j(topic = "cd-player")
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
        states.withStates()
                .initial(States.IDLE)
                .state(States.IDLE)
                .and()

                .withStates()
                .parent(States.IDLE)
                .initial(States.CLOSED)
                .state(States.CLOSED, closedEntryAction(), null)
                .state(States.OPEN)
                .and()

                .withStates()
                .state(States.BUSY)
                .and()

                .withStates()
                .parent(States.BUSY)
                .initial(States.PLAYING)
                .state(States.PLAYING)
                .state(States.PAUSED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                .withExternal()
                .source(States.CLOSED).target(States.OPEN).event(Events.EJECT)
                .and()

                .withExternal()
                .source(States.OPEN).target(States.CLOSED).event(Events.EJECT)
                .and()

                .withExternal()
                .source(States.OPEN).target(States.CLOSED).event(Events.PLAY)
                .and()

                .withExternal()
                .source(States.PLAYING).target(States.PAUSED).event(Events.PAUSE)
                .and()

                .withInternal()
                .source(States.PLAYING)
                .action(playingAction())
                .timer(1000)
                .and()

                .withInternal()
                .source(States.PLAYING).event(Events.BACK)
                .action(trackAction())
                .and()

                .withInternal()
                .source(States.PLAYING).event(Events.FORWARD)
                .action(trackAction())
                .and()

                .withExternal()
                .source(States.PAUSED).target(States.PLAYING).event(Events.PAUSE)
                .and()

                .withExternal()
                .source(States.BUSY).target(States.IDLE).event(Events.STOP)
                .and()

                .withExternal()
                .source(States.IDLE).target(States.BUSY).event(Events.PLAY)
                .action(playAction())
                .guard(playGuard())
                .and()

                .withInternal()
                .source(States.OPEN).event(Events.LOAD).action(loadAction());
    }

    @Bean
    public ClosedEntryAction closedEntryAction() {
        return new ClosedEntryAction();
    }

    @Bean
    public LoadAction loadAction() {
        return new LoadAction();
    }

    @Bean
    public TrackAction trackAction() {
        return new TrackAction();
    }

    @Bean
    public PlayAction playAction() {
        return new PlayAction();
    }

    @Bean
    public PlayingAction playingAction() {
        return new PlayingAction();
    }

    @Bean
    public PlayGuard playGuard() {
        return new PlayGuard();
    }
}
