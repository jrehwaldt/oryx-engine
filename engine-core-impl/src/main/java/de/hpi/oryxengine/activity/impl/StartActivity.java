package de.hpi.oryxengine.activity.impl;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.processInstance.ProcessInstance;

/**
 * The Class StartActivity.
 * The now empty behaviour of a startnode.
 */
public class StartActivity implements Activity {

    /** 
     * Empty behaviour because now we don't do anything.
     * 
     *  TODO: Make it not empty. Maybe something needs to be done.
     *   
     * @param instance
     *            the processinstance (to get the variable)
     * @see de.hpi.oryxengine.activity.Activity#execute(de.hpi.oryxengine.processInstance.ProcessInstance)
     */
    public void execute(ProcessInstance instance) {

        // Nothing toDo
    }

}
