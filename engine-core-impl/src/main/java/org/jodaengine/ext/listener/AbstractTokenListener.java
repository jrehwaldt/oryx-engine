package org.jodaengine.ext.listener;

import java.util.Observable;

import javax.annotation.Nonnull;

import org.jodaengine.ext.ObserverListener;
import org.jodaengine.plugin.activity.ActivityLifecycleChangeEvent;
import org.jodaengine.plugin.activity.ActivityLifecyclePlugin;

/**
 * This class may be injected to observe
 * the lifecycle of certain activities.
 */
public abstract class AbstractTokenListener
implements ObserverListener, ActivityLifecyclePlugin {
    
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
