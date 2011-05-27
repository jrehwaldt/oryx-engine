package org.jodaengine.process.instance;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;

/**
 * The Interface ProcessInstanceContext. It is shared between Tokens that work in the same process scope, for example in
 * the same subprocess. This makes it possible to synchronize them at parallel joins and share process instance
 * variables.
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public interface ProcessInstanceContext {

    /**
     * Signals that the declared transition has been taken.
     * If all transitions (e.g. for an AND-join) have been signaled the process can continue execution.
     * 
     * @param t
     *            the transition that is signaled
     */
    void setWaitingExecution(Transition t);

    /**
     * Gets all transitions that are incoming to a node and that have been signaled.
     * 
     * @param n
     *            the node that the signaled transitions are checked for
     * @return a list of all incoming transitions that have been signaled so far
     */
    List<Transition> getWaitingExecutions(Node n);

    /**
     * Determines, whether all incoming transitions for the given node haven been signaled.
     * 
     * @param n
     *            the node that is checked
     * @return true, if successful
     */
    boolean allIncomingTransitionsSignaled(Node n);

    /**
     * Removes all incoming transitions of a node from the internal storage of all signaled incoming transitions.
     * This is used in the case, e.g. that the AND-Join triggers. Then the signaled transitions have to be reset.
     * 
     * @param n
     *            the node that triggered
     */
    void removeIncomingTransitions(Node n);

    /**
     * Sets the variable in the process context.
     * 
     * @param variableId
     *            the name
     * @param variableValue
     *            the value
     */
    void setVariable(String variableId, Object variableValue);

    /**
     * Retrieves the variable that is associated to the name.
     * 
     * @param name
     *            {@link String} representing the id of the variable that can be used for acces later
     * @return an {@link Object} representing the value of the variable
     * @see org.jodaengine.process.token.Token#getVariable(java.lang.String)
     */
    Object getVariable(String name);

    /**
     * Retrieves all defined variables.
     * <ul>
     * <li>Map Key: the variableID</li>
     * <li>Map Value: the variableValue</li>
     * </ul>
     * 
     * @return a {@link Map} with varibaleId and variableValues
     */
    Map<String, Object> getVariableMap();
}
