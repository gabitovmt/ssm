package org.example.ssm61;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.util.EnumSet;

@Configuration
public class Config {

    @Bean
    public String stateChartModel() throws IOException {
        var model = new ClassPathResource("statechartmodel.txt");
        var lines = Files.readAllLines(model.getFile().toPath());
        return String.join("\n", lines);
    }

    @Configuration
    @EnableStateMachine
    @Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public static class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

        @Override
        public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
            config.withConfiguration().autoStartup(true);
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
                    .withExternal().source(States.S0).target(States.S1).event(Events.A).and()
                    .withExternal().source(States.S1).target(States.S2).event(Events.B).and()
                    .withExternal().source(States.S2).target(States.S0).event(Events.C);
        }
    }
}
