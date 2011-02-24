package de.hpi.oryxengine.activity;

import de.hpi.oryxengine.processInstance.ProcessInstance;

/**
 * The Interface NodeInterface.
 */
public interface Activity {

    /**
     * Execute. Starts the execution of a Node Instance.
     */
    void execute(ProcessInstance instance);

}
