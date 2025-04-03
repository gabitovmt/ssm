package org.example.ssm59.actions;

import org.example.ssm59.domain.Cd;
import org.example.ssm59.domain.Events;
import org.example.ssm59.domain.States;
import org.example.ssm59.domain.Variables;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

public class PlayGuard implements Guard<States, Events> {

    @Override
    public boolean evaluate(StateContext<States, Events> context) {
        return context.getExtendedState().getVariables().containsKey(Variables.CD)
                && !context.getExtendedState().get(Variables.CD, Cd.class).tracks().isEmpty();
    }
}
