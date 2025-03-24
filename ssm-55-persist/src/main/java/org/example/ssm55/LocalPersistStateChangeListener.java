package org.example.ssm55;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

@RequiredArgsConstructor
public class LocalPersistStateChangeListener implements PersistStateMachineHandler.PersistStateChangeListener {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void onPersist(
            State<String, String> state,
            Message<String> message,
            Transition<String, String> transition,
            StateMachine<String, String> stateMachine
    ) {
        if (message == null || !message.getHeaders().containsKey("order")) {
            return;
        }

        Integer order = message.getHeaders().get("order", Integer.class);
        jdbcTemplate.update("update orders set state = ? where id = ?", state.getId(), order);
    }
}
