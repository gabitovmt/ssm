package org.example.ssm55.config;

import lombok.RequiredArgsConstructor;
import org.example.ssm55.Persist;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;

@Configuration
@RequiredArgsConstructor
public class PersistHandlerConfig {
    private final StateMachine<String, String> stateMachine;
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public Persist persist() {
        return new Persist(persistStateMachineHandler(), jdbcTemplate);
    }

    @Bean
    public PersistStateMachineHandler persistStateMachineHandler() {
        return new PersistStateMachineHandler(stateMachine);
    }
}
