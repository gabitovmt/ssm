package org.example.ssm58;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.security.SecurityRule;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
@Slf4j
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .and()

                .withSecurity()
                .enabled(true)
                .event("hasRole('USER')");
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states.withStates()
                .initial(States.S0)
                .states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                .withExternal()
                .source(States.S0).target(States.S1).event(Events.A)
                .and()

                .withExternal()
                .source(States.S1).target(States.S2).event(Events.B)
                .and()

                .withExternal()
                .source(States.S2).target(States.S0).event(Events.C)
                .and()

                .withExternal()
                .source(States.S2).target(States.S3).event(Events.E)
                .secured("ROLE_ADMIN", SecurityRule.ComparisonType.ANY)
                .and()

                .withExternal()
                .source(States.S3).target(States.S0).event(Events.C)
                .and()

                .withInternal()
                .source(States.S0).event(Events.D)
                .action(adminAction())
                .and()

                .withInternal()
                .source(States.S1).event(Events.F)
                .action(transitionAction())
                .secured("ROLE_ADMIN", SecurityRule.ComparisonType.ANY);
    }

    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    @Bean
    public Action<States, Events> adminAction() {
        return new Action<>() {
            @Secured("ROLE_ADMIN")
            @Override
            public void execute(StateContext<States, Events> context) {
                log.info("Executed only for admin role");
            }
        };
    }

    @Bean
    public Action<States, Events> transitionAction() {
        return context -> log.info("Executed only for admin role");
    }
}
