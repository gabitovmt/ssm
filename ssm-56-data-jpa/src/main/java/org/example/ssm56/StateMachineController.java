package org.example.ssm56;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.data.RepositoryTransition;
import org.springframework.statemachine.data.TransitionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.StreamSupport;

@Controller
@RequiredArgsConstructor
public class StateMachineController {
    private final StateMachineFactory<String, String> stateMachineFactory;
    private final TransitionRepository<? extends RepositoryTransition> transitionRepository;

    @RequestMapping("/")
    public String home() {
        return "redirect:/state";
    }

    @RequestMapping("/state")
    public String feedAndGetStates(
            @RequestParam(value = "events", required = false) List<String> events,
            Model model) {
        StateMachine<String, String> stateMachine = stateMachineFactory.getStateMachine();
        StateMachineLogListener listener = new StateMachineLogListener();
        stateMachine.addStateListener(listener);

        stateMachine.startReactively().block();
        if (events != null) {
            for (String event : events) {
                stateMachine.sendEvent(
                        Mono.just(MessageBuilder.withPayload(event).build())
                ).blockLast();
            }
        }
        stateMachine.stopReactively().block();

        model.addAttribute("allEvents", getEvents());
        model.addAttribute("messages", createMessage(listener.getMessages()));

        return "states";
    }

    private String[] getEvents() {
        return StreamSupport.stream(transitionRepository.findAll().spliterator(), false)
                .map(RepositoryTransition::getEvent)
                .toArray(String[]::new);
    }

    private String createMessage(List<String> messages) {
        return String.join("\n", messages);
    }
}
