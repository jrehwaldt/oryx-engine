package org.jodaengine;

import org.jodaengine.loadgenerator.LoadGenerator;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.navigator.NavigatorState;
import org.jodaengine.plugin.navigator.AbstractNavigatorListener;

/**
 * The listener interface for receiving schedulerListEmpty events. The class that is interested in processing a
 * schedulerListEmpty event implements this interface, and the object created with that class is registered with a
 * component using the component's <code>addSchedulerListEmptyListener</code> method. When the schedulerListEmpty event
 * occurs, that object's appropriate method is invoked.
 * 
 * @see SchedulerListEmptyEvent
 */
public final class NoRunningInstancesLoadgeneratorCaller extends AbstractNavigatorListener {

    /** The hugene. */
    private LoadGenerator hugene;
    /** determines whether or not we already called the method on the loadgenerator. */
    private boolean called;

    /**
     * Instantiates a new scheduler list empty listener.
     * 
     * @param gene
     *            the Load generator we want to report to.
     */
    public NoRunningInstancesLoadgeneratorCaller(LoadGenerator gene) {

        this.hugene = gene;
        called = false;
    }

    @Override
    protected synchronized void stateChanged(Navigator nav, NavigatorState navState) {

        if ((navState == NavigatorState.IDLE) && (!called)) {
            hugene.navigatorCurrentlyFinished();
            called = true;
        }
    }
}
