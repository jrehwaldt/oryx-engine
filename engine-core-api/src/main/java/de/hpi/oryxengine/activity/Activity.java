package de.hpi.oryxengine.activity;

import de.hpi.oryxengine.processInstance.ProcessInstance;

/**
 * The Interface Activity.
 * An activity is the behaviour of a Node. So what the node does exactly.
 */
public interface Activity {

    /**
     * Execute. Starts the execution of the Activity.
     *
     * @param instance the processinstance which is needed because there might be valuable data in there.
     */
    void execute(ProcessInstance instance);

}
