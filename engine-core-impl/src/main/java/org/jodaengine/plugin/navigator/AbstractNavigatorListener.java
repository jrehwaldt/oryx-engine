package org.jodaengine.plugin.navigator;

import java.util.Observable;

import javax.annotation.Nonnull;

import org.jodaengine.navigator.Navigator;
import org.jodaengine.navigator.NavigatorState;
import org.jodaengine.plugin.ObserverPlugin;


/**
 * This class may be injected to observe the lifecycle of a navigator.
 *
 * @see AbstractNavigatorEvent
 */
public abstract class AbstractNavigatorListener
implements ObserverPlugin {
    
    /**
     * This method is called whenever the navigator changes its state.
     *  
     * {@inheritDoc}
     */
    @Override
    public void update(@Nonnull Observable observable,
                       @Nonnull Object param) {
        stateChanged((Navigator) observable, (NavigatorState) param);
    }
    
    /**
     * The state of the navigator changed.
     *
     *@param nav the Navigator
     * @param navState the state the navigator changed to.
     */
    protected abstract void stateChanged(Navigator nav, NavigatorState navState);
}
