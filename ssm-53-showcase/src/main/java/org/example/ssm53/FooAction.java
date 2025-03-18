package org.example.ssm53;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.util.Map;

@Slf4j
public class FooAction implements Action<States, Events> {

    @Override
    public void execute(StateContext<States, Events> context) {
        Map<Object, Object> variables = context.getExtendedState().getVariables();
        Integer foo = context.getExtendedState().get("foo", Integer.class);
        if (foo == null) {
            log.info("Init foo to 0");
            variables.put("foo", 0);
        } else if (foo == 0) {
            log.info("Switch foo to 1");
            variables.put("foo", 1);
        } else if (foo == 1) {
            log.info("Switch foo to 0");
            variables.put("foo", 0);
        }
    }
}
