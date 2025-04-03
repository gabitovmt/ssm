package org.example.ssm59.actions;

import org.example.ssm59.domain.Cd;
import org.example.ssm59.domain.Events;
import org.example.ssm59.domain.States;
import org.example.ssm59.domain.Variables;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

import java.time.LocalTime;

public class PlayAction implements Action<States, Events> {

    @Override
    public void execute(StateContext<States, Events> context) {
        Cd cd = context.getExtendedState().get(Variables.CD, Cd.class);

        context.getExtendedState().getVariables().put(Variables.ELAPSED_TIME, LocalTime.of(0, 0));
        context.getExtendedState().getVariables().put(Variables.TRACK, cd.tracks().get(0));
    }
}
