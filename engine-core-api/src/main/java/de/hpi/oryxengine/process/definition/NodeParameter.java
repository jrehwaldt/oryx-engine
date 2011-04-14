package de.hpi.oryxengine.process.definition;

import de.hpi.oryxengine.process.structure.ActivityBlueprint;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;

/**
 * The Interface NodeParameter. This is used for the ProcessBuilder to build Nodes.
 * 
 * @author Thorben
 */
public interface NodeParameter {

    /**
     * Gets the activity blueprint.
     * 
     * @return the activity blueprint
     */
    ActivityBlueprint getActivityBlueprint();

    /**
     * Gets the incoming behaviour.
     * 
     * @return the incoming behaviour
     */
    IncomingBehaviour getIncomingBehaviour();

    /**
     * Gets the outgoing behaviour.
     * 
     * @return the outgoing behaviour
     */
    OutgoingBehaviour getOutgoingBehaviour();
}
