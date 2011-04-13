package de.hpi.oryxengine.process.instance;

import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;

/**
 * The Interface ProcessInstanceContext. It is shared between Tokens that work in the same process scope, for example in
 * the same subprocess. This makes it possible to synchronize them at parallel joins and share process instance
 * variables.
 */
public interface ProcessInstanceContext {

    /**
     * Sets the waiting execution.
     * 
     * @param t
     *            the new waiting execution
     */
    void setWaitingExecution(Transition t);

    /**
     * Gets the waiting executions.
     * 
     * @param n
     *            the Node
     * @return the waiting executions
     */
    List<Transition> getWaitingExecutions(Node n);

    /**
     * Determines, whether all incoming transitions for the given node haven been signaled.
     * 
     * @param n
     *            the n
     * @return true, if successful
     */
    boolean allIncomingTransitionsSignaled(Node n);

    /**
     * Removes the incoming transitions.
     * 
     * @param n
     *            the n
     */
    void removeIncomingTransitions(Node n);

    /**
     * Sets the variable.
     * 
     * @param name
     *            the name
     * @param value
     *            the value
     */
    void setVariable(String name, Object value);

    /**
     * Gets the variable.
     * 
     * @param name
     *            the name
     * @return the variable
     * @see de.hpi.oryxengine.process.token.Token#getVariable(java.lang.String)
     */
    Object getVariable(String name);

    /**
     * Sets the activity parameters for the given node on this instance. These parameters are used for the constructor
     * of the activity class that is provided with this node.
     * 
     * @param nodeID
     *            the node id
     * @param params
     *            the params
     */
    void setActivityParameters(UUID nodeID, Object[] params);

    /**
     * Gets the activity parameters. Can be used to instantiate the activity class associated with this node.
     * 
     * @param nodeID
     *            the node id
     * @return the activity parameters
     */
    Object[] getActivityParameters(UUID nodeID);
    
    /**
     * Sets the classes that make up the constructor to use, when the activity is to be instantiated.
     * Note: The order of the classes is important!
     *
     * @param nodeID the node id
     * @param constructorClasses the constructor classes
     */
    void setActivityConstructorClasses(UUID nodeID, Class<?>[] constructorClasses);
    
    /**
     * Gets the classes that make up the signature of the constructor to use to instantiate the nodes activity.
     *
     * @param nodeID the node id
     * @return the activity constructor classes
     */
    Class<?>[] getActivityConstructorClasses(UUID nodeID);
}
