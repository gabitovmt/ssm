package org.example.ssm56;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineModelConfigurer;
import org.springframework.statemachine.config.model.StateMachineModelFactory;
import org.springframework.statemachine.data.*;
import org.springframework.statemachine.data.support.StateMachineJackson2RepositoryPopulatorFactoryBean;

@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class StateMachineConfig extends StateMachineConfigurerAdapter<String, String> {
    private final StateRepository<? extends RepositoryState> stateRepository;
    private final TransitionRepository<? extends RepositoryTransition> transitionRepository;

    @Override
    public void configure(StateMachineModelConfigurer<String, String> model) throws Exception {
        model.withModel().factory(modelFactory());
    }

    @Bean
    public StateMachineModelFactory<String, String> modelFactory() {
        return new RepositoryStateMachineModelFactory(stateRepository, transitionRepository);
    }

    @Bean
    public StateMachineJackson2RepositoryPopulatorFactoryBean jackson2RepositoryPopulatorFactoryBean() {
        var factoryBean = new StateMachineJackson2RepositoryPopulatorFactoryBean();
        factoryBean.setResources(new Resource[]{new ClassPathResource("data.json")});

        return factoryBean;
    }
}
