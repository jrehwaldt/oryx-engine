package de.hpi.oryxengine.process.definition;

import de.hpi.oryxengine.activity.Activity;
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
     * @param clazz the new activity class
     */
    void setActivityClass(Class<? extends Activity> clazz);
    
    /**
     * Gets the activity.
     *
     * @return the activity's class
     */
    Class<? extends Activity> getActivityClass();
    
    /**
     * Sets the incoming behaviour.
     *
     * @param behaviour the new incoming behaviour
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
     * @param behaviour the new outgoing behaviour
     */
    void setOutgoingBehaviour(OutgoingBehaviour behaviour);
    
    /**
     * Gets the outgoing behaviour.
     *
     * @return the outgoing behaviour
     */
    OutgoingBehaviour getOutgoingBehaviour();
    
}
