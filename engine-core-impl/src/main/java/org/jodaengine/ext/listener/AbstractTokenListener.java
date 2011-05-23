package org.jodaengine.ext.listener;

import java.util.Observable;

import javax.annotation.Nonnull;

import org.jodaengine.ext.ObersverListener;
import org.jodaengine.ext.listener.token.ActivityLifecycleChangeEvent;
import org.jodaengine.ext.listener.token.ActivityLifecycleListener;

/**
 * This class may be injected to observe
 * the lifecycle of certain activities.
 */
public abstract class AbstractTokenListener
implements ObersverListener, ActivityLifecycleListener {
    
    /**
     * This method is invoked whenever the activity's state changes.
     * 
     * {@inheritDoc}
     */
    @Override
    public void update(@Nonnull Observable observable,
                       @Nonnull Object event) {
        stateChanged((ActivityLifecycleChangeEvent) event);
    }
}
