package org.jodaengine.node.helper;

import javax.annotation.Nonnull;

import org.jodaengine.ext.listener.AbstractTokenListener;
import org.jodaengine.node.activity.ActivityState;
import org.jodaengine.plugin.activity.ActivityLifecycleChangeEvent;


/**
 * This class is a logger and may be injected to observe
 * the lifecycle of certain activities.
 */
public final class ActivityLifecycleAssurancePlugin
extends AbstractTokenListener {
    
    /** The completed called. */
    private boolean completedCalled;
    
    /**
     * Checks if is completed called.
     *
     * @return true, if is completed called
     */
    public boolean isCompletedCalled() {
    
        return completedCalled;
    }

    /**
     * Sets the completed called.
     *
     * @param completedCalled the new completed called
     */
    public void setCompletedCalled(boolean completedCalled) {
    
        this.completedCalled = completedCalled;
    }
    
    @Override
    public void stateChanged(@Nonnull ActivityLifecycleChangeEvent event) {
        if (event.getNewState() == ActivityState.COMPLETED) {
            this.completedCalled = true;   
        }
    }
    
}
