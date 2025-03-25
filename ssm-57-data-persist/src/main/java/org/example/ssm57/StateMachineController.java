package org.example.ssm57;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class StateMachineController {
    public static final String MACHINE_ID_1 = "datajpapersist1";
    public static final String MACHINE_ID_2 = "datajpapersist2";
    private static final String[] MACHINES = {MACHINE_ID_1, MACHINE_ID_2};

    private final StateMachineLogListener listener = new StateMachineLogListener();
    private StateMachine<States, Events> currentStateMachine;

    private final StateMachineService<States, Events> stateMachineService;
    private final StateMachinePersist<States, Events, String> stateMachinePersist;

    @RequestMapping("/")
    public String home() {
        return "redirect:/state";
    }

    @RequestMapping("/state")
    public String feedAndGetStates(
            @RequestParam(value = "events", required = false) List<Events> events,
            @RequestParam(value = "machine", required = false, defaultValue = MACHINE_ID_1) String machineId,
            Model model
    ) throws Exception {
        StateMachine<States, Events> stateMachine = getStateMachine(machineId);
        if (events != null) {
            for (Events event : events) {
                stateMachine.sendEvent(
                        Mono.just(MessageBuilder.withPayload(event).build())
                ).blockLast();
            }
        }

        StateMachineContext<States, Events> stateMachineContext = stateMachinePersist.read(machineId);
        var context = Objects.requireNonNullElse(stateMachineContext, "").toString();

        model.addAttribute("allMachines", MACHINES);
        model.addAttribute("allEvents", getEvents());
        model.addAttribute("machine", machineId);
        model.addAttribute("messages", createMessage(listener.getMessages()));
        model.addAttribute("context", context);

        return "states";
    }

    private synchronized StateMachine<States, Events> getStateMachine(String machineId) {
        listener.resetMessages();

        if (currentStateMachine != null) {
            if (ObjectUtils.nullSafeEquals(currentStateMachine.getId(), machineId)) {
                return currentStateMachine;
            }

            stateMachineService.releaseStateMachine(currentStateMachine.getId());
        }

        currentStateMachine = stateMachineService.acquireStateMachine(machineId);
        currentStateMachine.addStateListener(listener);

        return currentStateMachine;
    }

    private Events[] getEvents() {
        return EnumSet.allOf(Events.class).toArray(new Events[0]);
    }

    private String createMessage(List<String> messages) {
        return String.join("\n", messages);
    }
}
