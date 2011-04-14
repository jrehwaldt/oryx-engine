package de.hpi.oryxengine.process.definition;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.process.structure.ActivityBlueprint;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * The Class NodeParameterImpl. Have a look at the interface {@link NodeParameter}.
 * 
 * @author thorben
 */
public class NodeParameterImpl implements NodeParameter {
    private ActivityBlueprint blueprint;
    private OutgoingBehaviour outgoing;
    private IncomingBehaviour incoming;

    /**
     * Instantiates a new node parameter impl.
     * 
     * @param blueprint
     *            the blueprint
     * @param incoming
     *            the incoming
     * @param outgoing
     *            the outgoing
     */
    public NodeParameterImpl(ActivityBlueprint blueprint, IncomingBehaviour incoming, OutgoingBehaviour outgoing) {

        this.blueprint = blueprint;
        this.incoming = incoming;
        this.outgoing = outgoing;
    }

    /**
     * This is a convenience constructor, if you only need the activity's default constructor.
     * 
     * @param clazz
     *            the clazz
     * @param incoming
     *            the incoming
     * @param outgoing
     *            the outgoing
     */
    public NodeParameterImpl(Class<? extends Activity> clazz, IncomingBehaviour incoming, OutgoingBehaviour outgoing) {

        this.incoming = incoming;
        this.outgoing = outgoing;
        this.blueprint = new ActivityBlueprintImpl(clazz);
    }

    /**
     * Instantiates a new node parameter impl. This is just for convenience, might be removed later.
     */
    public NodeParameterImpl() {

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

    @Override
    public void setActivityBlueprint(ActivityBlueprint blueprint) {

        this.blueprint = blueprint;

    }

    @Override
    public ActivityBlueprint getActivityBlueprint() {

        return blueprint;
    }

    @Override
    public void setActivityClassOnly(Class<? extends Activity> clazz) {

        this.blueprint = new ActivityBlueprintImpl(clazz);

    }

}
