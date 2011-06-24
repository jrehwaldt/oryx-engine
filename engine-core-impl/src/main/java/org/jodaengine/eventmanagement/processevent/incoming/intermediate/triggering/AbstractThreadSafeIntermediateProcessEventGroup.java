package org.jodaengine.eventmanagement.processevent.incoming.intermediate.triggering;

import org.jodaengine.eventmanagement.processevent.incoming.TriggeringBehavior;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.IncomingIntermediateProcessEvent;
import org.jodaengine.eventmanagement.subscription.IncomingProcessEvent;
import org.jodaengine.process.token.Token;

/**
 * This process event group is designed to provide certain functionality for grouping
 * {@link IncomingIntermediateProcessEvent}.
 * <p>
 * It is implemented thread safe and it is supposed to be called only once. All following calls are ignored.
 * </p>
 */
public abstract class AbstractThreadSafeIntermediateProcessEventGroup extends AbstractProcessEventGroup {

    protected boolean called = false;
    
    /**
     * Default Constructor.
     * 
     * @param token
     *            - the {@link Token} that should be resumed
     */
    protected AbstractThreadSafeIntermediateProcessEventGroup(Token token) {

        super(token);
    }

    @Override
    public synchronized void trigger(IncomingProcessEvent processEvent) {

        // If it was already called then leave right now
        if (called) {
            this.called = true;
            return;
        }

        triggerIntern((IncomingIntermediateProcessEvent) processEvent);
    }

    /**
     * If an {@link IncomingProcessEvent} that belongs to that {@link TriggeringBehavior} is triggered than this method
     * is called.
     * 
     * @param processIntermediateEvent
     *            - the {@link IncomingIntermediateProcessEvent} that was triggered
     */
    protected abstract void triggerIntern(IncomingIntermediateProcessEvent processIntermediateEvent);
}
