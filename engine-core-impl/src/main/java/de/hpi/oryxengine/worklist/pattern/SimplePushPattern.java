package de.hpi.oryxengine.worklist.pattern;

import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.worklist.Pattern;
import de.hpi.oryxengine.worklist.Task;
import de.hpi.oryxengine.worklist.WorklistQueue;

// TODO: Auto-generated Javadoc
/**
 * Simple Push Pattern - Only for testing.
 */
public class SimplePushPattern implements Pattern {

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Task task, Token token, WorklistQueue worklistService) {

        System.out.println("Habe es in List gepackt, du Otto^^.'");
    }

}
