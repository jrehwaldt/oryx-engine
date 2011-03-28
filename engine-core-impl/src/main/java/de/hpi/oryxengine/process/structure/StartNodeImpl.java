package de.hpi.oryxengine.process.structure;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class StartNodeImpl.
 */
public class StartNodeImpl extends NodeImpl implements StartNode {
    private StartEvent event;

    /**
     * Instantiates a new start node impl.
     * 
     * @param event
     *            the event
     */
    public StartNodeImpl(StartEvent event) {
        
        // Use standard parameters for the start event.
        super(new NullActivity(), new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
        this.event = event;
    }

    @Override
    public StartEvent getStartEvent() {

        return event;
    }

}
