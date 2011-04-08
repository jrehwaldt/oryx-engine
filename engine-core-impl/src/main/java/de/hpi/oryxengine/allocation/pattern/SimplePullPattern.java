package de.hpi.oryxengine.allocation.pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.allocation.Pattern;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.allocation.TaskAllocation;
import de.hpi.oryxengine.process.token.Token;

/**
 * Simple Pull Pattern - Only for testing.
 */
public class SimplePullPattern implements Pattern {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(Task task, Token token, TaskAllocation worklistService) {

        logger.info("Habe es aus der Liste genommen, Juuuuuunge!");
    }

}
