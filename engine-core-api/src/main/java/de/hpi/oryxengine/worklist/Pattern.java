package de.hpi.oryxengine.worklist;

import de.hpi.oryxengine.process.token.Token;

// TODO: Auto-generated Javadoc
/**
 * Represents a pattern object for distribution of a task into worklists.
 */
public interface Pattern {

    /**
     * Executes the pattern and performs the logic of the distribution.
     * 
     * @param task
     *            - the {@link Task} to distribute
     * @param token
     *            - reference to the {@link Token} in order to have more context information
     * @param worklistService
     *            - reference to the {@link WorklistQueue} in order to operate on worklist queues
     */
    void execute(Task task, Token token, WorklistQueue worklistService);
}
