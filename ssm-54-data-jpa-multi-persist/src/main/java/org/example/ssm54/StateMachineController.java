package org.example.ssm54;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.data.RepositoryTransition;
import org.springframework.statemachine.data.TransitionRepository;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StateMachineController {
    public static final String MACHINE_ID_1 = "datajpamultipersist1";
    public static final String MACHINE_ID_2 = "datajpamultipersist2";
    public static final String MACHINE_ID_2R1 = "datajpamultipersist2#R1";
    public static final String MACHINE_ID_2R2 = "datajpamultipersist2#R2";
    private static final String[] MACHINES = {MACHINE_ID_1, MACHINE_ID_2};

    private final StateMachineLogListener listener = new StateMachineLogListener();
    private final StateMachineService<String, String> stateMachineService;
    private final StateMachinePersist<String, String, String> stateMachinePersist;
    private final TransitionRepository<? extends RepositoryTransition> transitionRepository;

    private StateMachine<String, String> currentStateMachine;

    @RequestMapping("/")
    public String home() {
        return "redirect:/state";
    }

    @RequestMapping("/state")
    public String feedAndGetStates(
            @RequestParam(value = "events", required = false) List<String> events,
            @RequestParam(value = "machine", required = false, defaultValue = MACHINE_ID_1) String machineId,
            Model model
    ) throws Exception {
        StateMachine<String, String> stateMachine = getStateMachine(machineId);
        if (events != null) {
            for (String event : events) {
                stateMachine
                        .sendEvent(Mono.just(MessageBuilder.withPayload(event).build()))
                        .blockLast();
            }
        }

        model.addAttribute("allMachines", MACHINES);
        model.addAttribute("machine", machineId);
        model.addAttribute("currentMachine", currentStateMachine);
        model.addAttribute("allEvents", getEvents());
        model.addAttribute("messages", createMessages(listener.getMessages()));
        model.addAttribute("context", getStateMachineContext(machineId));

        return "states";
    }

    private String getStateMachineContext(String machineId) throws Exception {
        StringBuilder contextBuf = new StringBuilder();

        StateMachineContext<String, String> stateMachineContext = stateMachinePersist.read(machineId);
        if (stateMachineContext != null) {
            contextBuf.append(stateMachineContext);
        }

        if (!ObjectUtils.nullSafeEquals(machineId, MACHINE_ID_2)) {
            return contextBuf.toString();
        }

        stateMachineContext = stateMachinePersist.read(MACHINE_ID_2R1);
        if (stateMachineContext != null) {
            contextBuf.append("\n---\n");
            contextBuf.append(stateMachineContext);
        }
        stateMachineContext = stateMachinePersist.read(MACHINE_ID_2R2);
        if (stateMachineContext != null) {
            contextBuf.append("\n---\n");
            contextBuf.append(stateMachineContext);
        }

        return contextBuf.toString();
    }

    private synchronized StateMachine<String, String> getStateMachine(String machineId) {
        listener.resetMessages();
        if (currentStateMachine != null &&
                !ObjectUtils.nullSafeEquals(machineId, currentStateMachine.getState().getId())) {
            stateMachineService.releaseStateMachine(currentStateMachine.getId());
            currentStateMachine.stopReactively().block();
        }

        currentStateMachine = stateMachineService.acquireStateMachine(machineId, false);
        currentStateMachine.addStateListener(listener);
        currentStateMachine.startReactively().block();

        return currentStateMachine;
    }

    private String[] getEvents() {
        List<String> events = new ArrayList<>();
        for (RepositoryTransition transition : transitionRepository.findAll()) {
            events.add(transition.getEvent());
        }

        return events.toArray(new String[0]);
    }

    private String createMessages(List<String> messages) {
        return String.join("\n", messages);
    }
}
