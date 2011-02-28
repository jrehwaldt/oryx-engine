package de.hpi.oryxengine.activity.impl;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * The Class EndActivity.
 * Just the activity which gets executed on the endevent.
 * So nothing is done, in the future maybe more should be done.
 */
public class EndActivity implements Activity {

    /** 
     * Currently das nothing. Subject to chance.
     * TODO: what changes?
     * 
     * @param instance the processinstance id (may need it in order to perform actions)
     * @see de.hpi.oryxengine.activity.Activity#execute(de.hpi.oryxengine.process.instance.ProcessInstance)
     */
    public void execute(ProcessInstance instance) {

        // Doing nothing is the default behavior
        // This must change.
    }

}
