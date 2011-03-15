package de.hpi.oryxengine.plugin.scheduler;

import de.hpi.oryxengine.loadgenerator.LoadGenerator;
import de.hpi.oryxengine.process.token.Token;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving schedulerListEmpty events.
 * The class that is interested in processing a schedulerListEmpty
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addSchedulerListEmptyListener</code> method. When
 * the schedulerListEmpty event occurs, that object's appropriate
 * method is invoked.
 *
 * @see SchedulerListEmptyEvent
 */
public final class SchedulerEmptyListener 
extends AbstractSchedulerListener {

    /** The hugene. */
    private LoadGenerator hugene;    

    /**
     * Instantiates a new scheduler list empty listener.
     *
     * @param gene the Load generator we want to report to.
     */
    public SchedulerEmptyListener(LoadGenerator gene) {
        this.hugene = gene;
    }
    
    /**
     * But here it does nothing.
     *
     * @param numberOfTokens the number of tokens
     * @param token the token
     * {@inheritDoc}
     */
    @Override
    public void processInstanceSubmitted(int numberOfTokens, Token token) {

        // does nothing
    }

    /**
     * Process instance retrieved.
     *
     * @param numberOfTokens the number of tokens
     * @param token the token
     * {@inheritDoc}
     * Calls the method schedulerIsEmpty() in hugene, our loadgenerator
     * currently this is used as a signal that the execution of the instances
     * is finished.
     */
    @Override
    public void processInstanceRetrieved(int numberOfTokens, Token token) {

        if (numberOfTokens == 0) {
            this.hugene.schedulerIsEmpty();
        }

    }

}
