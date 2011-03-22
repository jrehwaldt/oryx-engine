package de.hpi.oryxengine.process.definition;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * The Class NodeParameterImpl.
 * 
 * @author thorben
 */
public class NodeParameterImpl implements NodeParameter {
    private Activity activity;
    private OutgoingBehaviour outgoing;
    private IncomingBehaviour incoming;

    /**
     * Instantiates a new node parameter impl.
     * 
     * @param activity
     *            the activity
     * @param incoming
     *            the incoming
     * @param outgoing
     *            the outgoing
     */
    public NodeParameterImpl(Activity activity, IncomingBehaviour incoming, OutgoingBehaviour outgoing) {

        this.activity = activity;
        this.incoming = incoming;
        this.outgoing = outgoing;
    }

    /**
     * Instantiates a new node parameter impl. This is just for convenience, might be removed later.
     */
    public NodeParameterImpl() {

    }

    @Override
    public void setActivity(Activity a) {

        this.activity = a;

    }

    @Override
    public Activity getActivity() {

        return activity;
    }

    @Override
    public void setIncomingBehaviour(IncomingBehaviour behaviour) {

        this.incoming = behaviour;

    }

    @Override
    public IncomingBehaviour getIncomingBehaviour() {

        return incoming;
    }

    @Override
    public void setOutgoingBehaviour(OutgoingBehaviour behaviour) {

        this.outgoing = behaviour;

    }

    @Override
    public OutgoingBehaviour getOutgoingBehaviour() {

        return outgoing;
    }

}
