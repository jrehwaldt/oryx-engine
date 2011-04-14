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

}
