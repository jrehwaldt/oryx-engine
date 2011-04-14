package de.hpi.oryxengine.process.definition;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.process.structure.ActivityBlueprint;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * The Interface NodeParameter. This is used for the ProcessBuilder to build Nodes.
 * 
 * @author thorben
 */
public interface NodeParameter {

    /**
     * Sets the activity.
     * 
     * @param blueprint
     *            the new activity blueprint
     */
    void setActivityBlueprint(ActivityBlueprint blueprint);

    /**
     * Sets the activity class only and uses the empty default constructor upon instantiation. This is a convenience
     * method.
     * 
     * @param clazz
     *            the new activity class
     */
    void setActivityClassOnly(Class<? extends Activity> clazz);

    /**
     * Gets the activity blueprint.
     * 
     * @return the activity blueprint
     */
    ActivityBlueprint getActivityBlueprint();

    /**
     * Sets the incoming behaviour.
     * 
     * @param behaviour
     *            the new incoming behaviour
     */
    void setIncomingBehaviour(IncomingBehaviour behaviour);

    /**
     * Gets the incoming behaviour.
     * 
     * @return the incoming behaviour
     */
    IncomingBehaviour getIncomingBehaviour();

    /**
     * Sets the outgoing behaviour.
     * 
     * @param behaviour
     *            the new outgoing behaviour
     */
    void setOutgoingBehaviour(OutgoingBehaviour behaviour);

    /**
     * Gets the outgoing behaviour.
     * 
     * @return the outgoing behaviour
     */
    OutgoingBehaviour getOutgoingBehaviour();

}
