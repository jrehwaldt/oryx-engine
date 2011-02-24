package de.hpi.oryxengine.activity;

import de.hpi.oryxengine.processInstance.ProcessInstance;

/**
 * The Interface NodeInterface.
 */
public interface Activity {

    /**
     * Execute. Starts the execution of a Node Instance.
     *
     * @param instance the processinstance which is needed because there might be valuable data in there.
     */
    void execute(ProcessInstance instance);

}
