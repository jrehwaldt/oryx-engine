package de.hpi.oryxengine.process.structure;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * The Class StartNodeImpl.
 */
public class StartNodeImpl extends NodeImpl implements StartNode {
    private StartEvent event;

    /**
     * Instantiates a new start node impl.
     * 
     * @param activity
     *            the activity
     * @param incomingBehaviour
     *            the incoming behaviour
     * @param outgoingBehaviour
     *            the outgoing behaviour
     * @param event
     *            the event
     */
    public StartNodeImpl(Activity activity,
                         IncomingBehaviour incomingBehaviour,
                         OutgoingBehaviour outgoingBehaviour,
                         StartEvent event) {

        super(activity, incomingBehaviour, outgoingBehaviour);
    }

    @Override
    public StartEvent getStartEvent() {

        return event;
    }

}
