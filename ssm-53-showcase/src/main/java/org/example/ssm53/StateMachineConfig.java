package org.example.ssm53;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

    @Bean
    public EventListener eventListener() {
        return new EventListener();
    }

    @Bean
    public FooGuard foo0Guard() {
        return new FooGuard(0);
    }

    @Bean
    public FooGuard foo1Guard() {
        return new FooGuard(1);
    }

    @Bean
    public FooAction fooAction() {
        return new FooAction();
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states.withStates()
            .initial(States.S0, fooAction())
            .state(States.S0)
            .and()
            .withStates()
                .parent(States.S0)
                .initial(States.S1)
                .state(States.S1)
                .and()
                .withStates()
                    .parent(States.S1)
                    .initial(States.S11)
                    .state(States.S11)
                    .state(States.S12)
                    .and()
                .withStates()
                    .parent(States.S0)
                    .state(States.S2)
                    .and()
                    .withStates()
                        .parent(States.S2)
                        .initial(States.S21)
                        .state(States.S21)
                        .and()
                        .withStates()
                            .parent(States.S21)
                            .initial(States.S211)
                            .state(States.S211)
                            .state(States.S212);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                .withExternal()
                    .source(States.S1).target(States.S1).event(Events.A)
                    .guard(foo1Guard())
                    .and()
                .withExternal()
                    .source(States.S1).target(States.S11).event(Events.B)
                    .and()
                .withExternal()
                    .source(States.S21).target(States.S211).event(Events.B)
                    .and()
                .withExternal()
                    .source(States.S1).target(States.S2).event(Events.C)
                    .and()
                .withExternal()
                    .source(States.S2).target(States.S1).event(Events.C)
                    .and()
                .withExternal()
                    .source(States.S1).target(States.S0).event(Events.D)
                    .and()
                .withExternal()
                    .source(States.S211).target(States.S21).event(Events.D)
                    .and()
                .withExternal()
                    .source(States.S0).target(States.S211).event(Events.E)
                    .and()
                .withExternal()
                    .source(States.S1).target(States.S211).event(Events.F)
                    .and()
                .withExternal()
                    .source(States.S2).target(States.S11).event(Events.F)
                    .and()
                .withExternal()
                    .source(States.S11).target(States.S211).event(Events.G)
                    .and()
                .withExternal()
                    .source(States.S211).target(States.S0).event(Events.G)
                    .and()
                .withInternal()
                    .source(States.S0).event(Events.H)
                    .guard(foo0Guard())
                    .action(fooAction())
                    .and()
                .withInternal()
                    .source(States.S2).event(Events.H)
                    .guard(foo1Guard())
                    .action(fooAction())
                    .and()
                .withInternal()
                    .source(States.S1).event(Events.H)
                    .and()
                .withExternal()
                    .source(States.S11).target(States.S12).event(Events.I)
                    .and()
                .withExternal()
                    .source(States.S211).target(States.S212).event(Events.I)
                    .and()
                .withExternal()
                    .source(States.S12).target(States.S212).event(Events.I);

    }
}
