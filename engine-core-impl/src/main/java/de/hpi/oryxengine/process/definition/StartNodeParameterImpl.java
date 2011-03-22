package de.hpi.oryxengine.process.definition;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * The Class StartNodeParameterImpl.
 */
public class StartNodeParameterImpl extends NodeParameterImpl implements StartNodeParameter {

    private StartEvent event;

    /**
     * Instantiates a new start node parameter impl.
     * 
     * @param activity
     *            the activity
     * @param incoming
     *            the incoming
     * @param outgoing
     *            the outgoing
     * @param event
     *            the event. Use null if you do not want to register any start event, for example if you have a BPMN
     *            blank start event.
     */
    public StartNodeParameterImpl(Activity activity,
                                  IncomingBehaviour incoming,
                                  OutgoingBehaviour outgoing,
                                  StartEvent event) {

        super(activity, incoming, outgoing);
        this.event = event;
    }

    /**
     * Instantiates a new start node parameter impl.
     */
    public StartNodeParameterImpl() {

    }

    @Override
    public void setStartEvent(StartEvent event) {

        this.event = event;

    }

    @Override
    public StartEvent getStartEvent() {

        return event;
    }

}
