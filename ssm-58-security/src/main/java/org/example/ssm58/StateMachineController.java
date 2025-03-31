package org.example.ssm58;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
@Slf4j
public class StateMachineController {
    private final StateMachine<States, Events> stateMachine;
    private final String stateChartModel;

    public StateMachineController(
            StateMachine<States, Events> stateMachine,
            @Qualifier("stateChartModel") String stateChartModel
    ) {
        this.stateMachine = stateMachine;
        this.stateChartModel = stateChartModel;
    }

    @RequestMapping("/")
    public String greeting() {
        return "redirect:/states";
    }

    @RequestMapping("/states")
    public String getStates(
            @RequestParam(value = "event", required = false) Events event,
            Model model
    ) {
        if (event != null) {
            try {
                stateMachine.sendEvent(
                        Mono.just(MessageBuilder.withPayload(event).build())
                ).blockLast();
            } catch (Exception e) {
                log.error("Error sendEvent", e);
            }
        }

        model.addAttribute("states", stateMachine.getState().getIds());
        model.addAttribute("stateChartModel", stateChartModel);

        return "states";
    }
}
