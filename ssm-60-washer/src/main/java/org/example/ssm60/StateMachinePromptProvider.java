package org.example.ssm60;

import org.jline.utils.AttributedString;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class StateMachinePromptProvider implements PromptProvider {

    @Override
    public AttributedString getPrompt() {
        return AttributedString.fromAnsi("sm>");
    }
}
