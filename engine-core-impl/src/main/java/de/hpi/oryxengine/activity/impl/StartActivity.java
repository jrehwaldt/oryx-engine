package de.hpi.oryxengine.activity.impl;

import de.hpi.oryxengine.activity.AbstractActivityImpl;
import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * The Class StartActivity.
 * The now empty behaviour of a startnode.
 */
public class StartActivity
extends AbstractActivityImpl {

    /**
     * Default constructor. Creates a new start activity.
     */
    public StartActivity() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeIntern(ProcessInstance instance) {
        // TODO: what is required here?
        // Nothing toDo
    }

}
