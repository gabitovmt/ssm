package org.example.ssm57;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

import java.util.EnumSet;

@Configuration
public class StateMachineConfig {

    @Bean
    public StateMachineRuntimePersister<States, Events, String> stateMachineRuntimePersister(
            JpaStateMachineRepository jpaStateMachineRepository
    ) {
        return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }

    @Configuration
    @EnableStateMachineFactory
    @RequiredArgsConstructor
    public static class MachineConfig extends StateMachineConfigurerAdapter<States, Events> {
        private final StateMachineRuntimePersister<States, Events, String> stateMachineRuntimePersister;

        @Override
        public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
            config.withPersistence().runtimePersister(stateMachineRuntimePersister);
        }

        @Override
        public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
            states.withStates()
                    .initial(States.S1)
                    .states(EnumSet.allOf(States.class));
        }

        @Override
        public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
            transitions
                    .withExternal().source(States.S1).target(States.S2).event(Events.E1).and()
                    .withExternal().source(States.S2).target(States.S3).event(Events.E2).and()
                    .withExternal().source(States.S3).target(States.S4).event(Events.E3).and()
                    .withExternal().source(States.S4).target(States.S5).event(Events.E4).and()
                    .withExternal().source(States.S5).target(States.S6).event(Events.E5).and()
                    .withExternal().source(States.S6).target(States.S1).event(Events.E6);
        }
    }

    @Configuration
    @RequiredArgsConstructor
    public static class ServiceConfig {
        private final StateMachineFactory<States, Events> stateMachineFactory;
        private final StateMachineRuntimePersister<States, Events, String> stateMachineRuntimePersister;

        @Bean
        public StateMachineService<States, Events> stateMachineService() {
            return new DefaultStateMachineService<>(stateMachineFactory, stateMachineRuntimePersister);
        }
    }
}
