package de.hpi.oryxengine.node.activity;

import de.hpi.oryxengine.process.token.Token;

/**
 * The Class StartActivity.
 * The now empty behaviour of a for instance startnode.
 */
public class NullActivity
extends AbstractActivity {

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
    public void executeIntern(Token instance) {
        // Nothing toDo
    }

}