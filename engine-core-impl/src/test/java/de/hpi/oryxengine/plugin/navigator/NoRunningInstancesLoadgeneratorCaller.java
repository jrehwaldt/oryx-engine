package de.hpi.oryxengine.plugin.navigator;

import de.hpi.oryxengine.loadgenerator.LoadGenerator;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.navigator.NavigatorState;
import de.hpi.oryxengine.plugin.navigator.AbstractNavigatorListener;
import de.hpi.oryxengine.process.token.Token;

/**
 * The listener interface for receiving schedulerListEmpty events.
 * The class that is interested in processing a schedulerListEmpty
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addSchedulerListEmptyListener</code> method. When
 * the schedulerListEmpty event occurs, that object's appropriate
 * method is invoked.
 *
 * @see SchedulerListEmptyEvent
 */
public final class NoRunningInstancesLoadgeneratorCaller 
extends AbstractNavigatorListener {

    /** The hugene. */
    private LoadGenerator hugene;    

    /**
     * Instantiates a new scheduler list empty listener.
     *
     * @param gene the Load generator we want to report to.
     */
    public NoRunningInstancesLoadgeneratorCaller(LoadGenerator gene) {
        this.hugene = gene;
    }
    

    @Override
    protected void stateChanged(Navigator nav, NavigatorState navState) {

        if (navState == NavigatorState.CURRENTLY_FINISHED) {
            hugene.navigatorCurrentlyFinished();
        }
        
    }

}
