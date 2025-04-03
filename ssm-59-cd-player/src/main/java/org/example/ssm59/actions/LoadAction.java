package org.example.ssm59.actions;

import org.example.ssm59.domain.Cd;
import org.example.ssm59.domain.Events;
import org.example.ssm59.domain.States;
import org.example.ssm59.domain.Variables;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class LoadAction implements Action<States, Events> {

    @Override
    public void execute(StateContext<States, Events> context) {
        Cd cd = context.getMessageHeaders().get(Variables.CD.name(), Cd.class);
        context.getExtendedState().getVariables().put(Variables.CD, cd);
    }
}
