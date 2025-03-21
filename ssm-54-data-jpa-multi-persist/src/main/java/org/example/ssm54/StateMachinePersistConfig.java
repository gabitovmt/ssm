package org.example.ssm54;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineModelConfigurer;
import org.springframework.statemachine.config.model.StateMachineModelFactory;
import org.springframework.statemachine.data.*;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class StateMachinePersistConfig extends StateMachineConfigurerAdapter<String, String> {
    private final StateRepository<? extends RepositoryState> stateRepository;
    private final TransitionRepository<? extends RepositoryTransition> transitionRepository;
    private final StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister;

    @Bean
    public StateMachineModelFactory<String, String> modelFactory() {
        return new RepositoryStateMachineModelFactory(stateRepository, transitionRepository);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        config.withPersistence().runtimePersister(stateMachineRuntimePersister);
    }

    @Override
    public void configure(StateMachineModelConfigurer<String, String> model) throws Exception {
        model.withModel().factory(modelFactory());
    }
}
