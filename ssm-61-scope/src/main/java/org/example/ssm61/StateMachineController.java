package org.example.ssm61;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
public class StateMachineController {
    private StateMachine<States, Events> stateMachine;
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
            stateMachine
                    .sendEvent(Mono.just(MessageBuilder.withPayload(event).build()))
                    .blockLast();
        }

        model.addAttribute("states", stateMachine.getState().getIds());
        model.addAttribute("stateChartModel", stateChartModel);

        return "states";
    }
}
