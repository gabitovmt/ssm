package org.example.ssm52;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.StateMachineEventResult.ResultType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class StateMachineController {
    private final StateMachine<States, Events> stateMachine;

    @GetMapping("/state")
    public Mono<States> state() {
        return Mono.defer(() -> Mono.justOrEmpty(stateMachine.getState().getId()));
    }

    @PostMapping("/events")
    public Flux<EventResult> events(@RequestBody Flux<EventData> eventData) {
        return eventData
                .filter(ed -> ed.getEvent() != null)
                .map(ed -> MessageBuilder.withPayload(ed.getEvent()).build())
                .flatMap(m -> stateMachine.sendEvent(Mono.just(m)))
                .map(EventResult::new);
    }

    @AllArgsConstructor
    @Getter
    public static class EventData {
        private final Events event;
    }

    @AllArgsConstructor
    public static class EventResult {
        private final StateMachineEventResult<States, Events> result;

        public ResultType getResultType() {
            return result.getResultType();
        }

        public Events getEvents() {
            return result.getMessage().getPayload();
        }
    }
}
