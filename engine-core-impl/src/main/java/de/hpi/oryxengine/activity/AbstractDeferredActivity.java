package de.hpi.oryxengine.activity;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.process.token.Token;

/**
 * The Class AbstractDeferredActivity.
 */
public abstract class AbstractDeferredActivity
extends AbstractActivity
implements DeferredActivity {
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@Nonnull Token token) {
        changeState(token, ActivityState.ACTIVE);
        executeIntern(token);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void signal(@Nonnull Token token) {
        changeState(token, ActivityState.COMPLETED);
    }

}
