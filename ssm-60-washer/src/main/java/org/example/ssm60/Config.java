package org.example.ssm60;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.shell.command.annotation.CommandScan;

import java.io.IOException;
import java.nio.file.Files;

@Configuration
@CommandScan
public class Config {

    @Bean
    public String stateChartModel() throws IOException {
        var model = new ClassPathResource("statechartmodel.txt");
        var lines = Files.readAllLines(model.getFile().toPath());
        return String.join("\n", lines);
    }
}
