package org.example.ssm04;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.StateMachineException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ErrorTest {

    @Autowired
    private StateMachine<String, String> stateMachine;

    @Test
    void testActionEntryErrorWithEvent() {
        StepVerifier.create(stateMachine.startReactively()).verifyComplete();
        assertThat(stateMachine.getState().getIds()).containsExactlyInAnyOrder("SI");

        StepVerifier.create(stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload("E1").build())))
                .consumeNextWith(result ->
                        StepVerifier.create(result.complete())
                                .consumeErrorWith(e -> assertThat(e)
                                        .isInstanceOf(StateMachineException.class)
                                        .cause()
                                        .hasMessageContaining("example error"))
                                .verify())
                .verifyComplete();

        assertThat(stateMachine.getState().getIds()).containsExactlyInAnyOrder("S1");
    }

    @Test
    void testActionEntryErrorWithEvent_notReactive() {
        stateMachine.startReactively().block();

        var message = MessageBuilder.withPayload("E1").build();
        StateMachineEventResult<String, String> result = stateMachine.sendEvent(Mono.just(message)).blockLast();
        assertThat(result).isNotNull();

        var complete = result.complete();
        assertThatThrownBy(complete::block)
                .isInstanceOf(StateMachineException.class)
                .cause()
                .hasMessageContaining("example error");
    }
}
