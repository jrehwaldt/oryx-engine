package de.hpi.oryxengine.worklist.pattern;

import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.worklist.Pattern;
import de.hpi.oryxengine.worklist.Task;
import de.hpi.oryxengine.worklist.WorklistQueue;

/**
 * Simple Push Pattern - Only for testing.
 */
public class SimplePushPattern implements Pattern {

    @Override
    public void execute(Task task, Token token, WorklistQueue worklistService) {

        System.out.println("Habe es in List gepackt, du Otto^^.'");
    }

}
