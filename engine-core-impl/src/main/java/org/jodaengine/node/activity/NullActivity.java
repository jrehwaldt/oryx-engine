package org.jodaengine.node.activity;

import org.jodaengine.process.token.BPMNToken;



/**
 * The Class StartActivity.
 * The now empty behaviour of a for instance startnode.
 */
public class NullActivity
extends AbstractBpmnActivity {

    /**
     * Default constructor. Creates a new start activity.
     */
    public NullActivity() {
        super();
    }

    /**
     * It's a null activity. It does nothing.
     * 
     * {@inheritDoc}
     */
    @Override
    public void executeIntern(BPMNToken instance) {
        // Nothing toDo
    }

}
