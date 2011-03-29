package de.hpi.oryxengine.allocation.pattern;

import de.hpi.oryxengine.allocation.Pattern;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.allocation.TaskAllocation;
import de.hpi.oryxengine.process.token.Token;

/**
 * Simple Pull Pattern - Only for testing.
 */
public class SimplePullPattern implements Pattern {

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Task task, Token token, TaskAllocation worklistService) {

        System.out.println("Habe es aus der Liste genommen, Juuuuuunge!");
    }

}
