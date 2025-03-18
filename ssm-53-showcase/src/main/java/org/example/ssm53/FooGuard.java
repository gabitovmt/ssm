package org.example.ssm53;

import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

@RequiredArgsConstructor
public class FooGuard implements Guard<States, Events> {
    private final int match;

    @Override
    public boolean evaluate(StateContext<States, Events> context) {
        Object foo = context.getExtendedState().getVariables().get("foo");
        return !(foo == null || !foo.equals(match));
    }
}
