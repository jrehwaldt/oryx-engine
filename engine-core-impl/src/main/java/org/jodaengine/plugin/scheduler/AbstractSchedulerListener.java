package org.jodaengine.plugin.scheduler;

import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jodaengine.navigator.schedule.SchedulerAction;
import org.jodaengine.navigator.schedule.SchedulerEvent;
import org.jodaengine.plugin.ObserverPlugin;


/**
 * The abstract Scheduler Listener should be extended from real Plugins to observe the scheduler.
 * 
 * The listener interface for receiving abstractScheduler events.
 * The class that is interested in processing a abstractScheduler
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addAbstractSchedulerListener</code> method. When
 * the abstractScheduler event occurs, that object's appropriate
 * method is invoked.
 *
 * @see AbstractSchedulerEvent
 */
public abstract class AbstractSchedulerListener implements ObserverPlugin, SchedulerListener {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Observable observable, Object param) {
        final SchedulerEvent event = (SchedulerEvent) param;
        SchedulerAction action = event.getSchedulerAction();
        
        switch (action) {
            case SUBMIT:
                processInstanceSubmitted(event.getNumberOfTokens(), event.getProcessToken());
                break;
            case RETRIEVE:
                processInstanceRetrieved(event.getNumberOfTokens(), event.getProcessToken());
                break;
            default:
                logger.error("We couldn't get the right action in the Scheduler listener for event", event);
        }
    }
}
