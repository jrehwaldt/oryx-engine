package de.hpi.oryxengine.process.definition;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * The Class NodeParameterImpl. Have a look at the interface {@link NodeParameter}.
 * 
 * @author thorben
 */
public class NodeParameterImpl implements NodeParameter {
    private Class<? extends Activity> clazz;
    private OutgoingBehaviour outgoing;
    private IncomingBehaviour incoming;

    /**
     * Instantiates a new node parameter impl.
     *
     * @param clazz the clazz
     * @param incoming the incoming
     * @param outgoing the outgoing
     */
    public NodeParameterImpl(Class<? extends Activity> clazz, IncomingBehaviour incoming, OutgoingBehaviour outgoing) {

        this.clazz = clazz;
        this.incoming = incoming;
        this.outgoing = outgoing;
    }

    /**
     * Instantiates a new node parameter impl. This is just for convenience, might be removed later.
     */
    public NodeParameterImpl() {

    }

    @Override
    public void setActivityClass(Class<? extends Activity> clazz) {

        this.clazz = clazz;

    }

    @Override
    public Class<? extends Activity> getActivityClass() {

        return clazz;
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
