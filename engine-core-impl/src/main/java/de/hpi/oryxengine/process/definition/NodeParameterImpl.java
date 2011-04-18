package de.hpi.oryxengine.process.definition;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.process.structure.ActivityBlueprint;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * The Class {@link NodeParameterImpl}. Have a look at the interface {@link NodeParameter}.
 * 
 * @author Thorben
 */
public class NodeParameterImpl implements NodeParameter {

    private ActivityBlueprint blueprint;
    private OutgoingBehaviour outgoingBehaviour;
    private IncomingBehaviour incomingBehaviour;

    /**
     * Instantiates a new {@link NodeParameterImpl}.
     * 
     * @param activityBlueprint
     *            - a {@link ActivityBlueprint}
     * @param incomingBehaviour
     *            - an {@link IncomingBehaviour}
     * @param outgoingBehaviour
     *            - an {@link OutgoingBehaviour}
     */
    public NodeParameterImpl(ActivityBlueprint activityBlueprint,
                             IncomingBehaviour incomingBehaviour,
                             OutgoingBehaviour outgoingBehaviour) {

        this.blueprint = activityBlueprint;
        this.incomingBehaviour = incomingBehaviour;
        this.outgoingBehaviour = outgoingBehaviour;
    }

    /**
     * This is a convenience constructor, if you only need the activity's default constructor.
     * 
     * @param activityClazz
     *            - an {@link Activity ActivityClass}
     * @param incomingBehaviour
     *            - an {@link IncomingBehaviour}
     * @param outgoingBehaviour
     *            - an {@link OutgoingBehaviour}
     */
    public NodeParameterImpl(Class<? extends Activity> activityClazz,
                             IncomingBehaviour incomingBehaviour,
                             OutgoingBehaviour outgoingBehaviour) {

        this.incomingBehaviour = incomingBehaviour;
        this.outgoingBehaviour = outgoingBehaviour;
        this.blueprint = new ActivityBlueprintImpl(activityClazz);
    }

    @Override
    public IncomingBehaviour getIncomingBehaviour() {

        return incomingBehaviour;
    }

    @Override
    public ActivityBlueprint getActivityBlueprint() {

        return blueprint;
    }

    @Override
    public OutgoingBehaviour getOutgoingBehaviour() {

        return outgoingBehaviour;
    }
}
