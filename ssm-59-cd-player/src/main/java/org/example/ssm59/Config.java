package org.example.ssm59;

import org.example.ssm59.cdplayer.CdPlayer;
import org.example.ssm59.domain.Events;
import org.example.ssm59.domain.Library;
import org.example.ssm59.domain.States;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.statemachine.StateMachine;

import java.io.IOException;
import java.nio.file.Files;

@Configuration
public class Config {

    @Bean
    public String stateChartModel() throws IOException {
        var model = new ClassPathResource("statechartmodel.txt");
        var lines = Files.readAllLines(model.getFile().toPath());
        return String.join("\n", lines);
    }

    @Bean
    public CdPlayer cdPlayer(StateMachine<States, Events> stateMachine) {
        return new CdPlayer(stateMachine);
    }

    @Bean
    public Library library() {
        return Library.buildSampleLibrary();
    }
}
