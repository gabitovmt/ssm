package org.example.ssm54;

import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.data.RepositoryTransition;
import org.springframework.statemachine.data.TransitionRepository;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            @RequestParam(value = "events")
            Model model
    ) throws Exception {
        return "states";
    }

    private String[] getEvents() {
        List<String> events = new ArrayList<>();
        for (RepositoryTransition transition : transitionRepository.findAll()) {
            events.add(transition.getEvent());
        }

        return events.toArray(new String[0]);
    }

    private String createMessage(List<String> messages) {
        return String.join("\n", messages);
    }
}
