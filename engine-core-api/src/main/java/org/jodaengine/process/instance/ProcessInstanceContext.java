package org.jodaengine.process.instance;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.structure.Node;

/**
 * The Interface ProcessInstanceContext. It is shared between Tokens that work in the same process scope, for example in
 * the same subprocess. This makes it possible to synchronize them at parallel joins and share process instance
 * variables.
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public interface ProcessInstanceContext {

    /**
     * Signals that the declared {@link ControlFlow} has been taken. Can be used to synchronize several control flows in
     * a control flow construct.
     * 
     * @param c
     *            the {@link ControlFlow} that is signaled
     */
    void setSignaledControlFlow(ControlFlow c);

    /**
     * Gets all {@link ControlFlow}s that are incoming to a node and that have been signaled.
     * 
     * @param n
     *            the node that the signaled {@link ControlFlow}s are checked for
     * @return a list of all incoming {@link ControlFlow}s that have been signaled so far
     */
    List<ControlFlow> getSignaledControlFlows(Node n);

    /**
     * Determines, whether all incoming {@link ControlFlow}s for the given node haven been signaled.
     * 
     * @param n
     *            the node that is checked
     * @return true, if successful
     */
    boolean allIncomingControlFlowsSignaled(Node n);

    /**
     * Removes all incoming {@link ControlFlow}s of a node from the internal storage of all
     * signaled incoming {@link ControlFlow}s.
     * This is used in the case, e.g. that the AND-Join triggers.
     * Then the signaled {@link ControlFlow}s have to be reset.
     * 
     * @param n
     *            the node that triggered
     */
    void removeSignaledControlFlows(Node n);

    /**
     * Removes the signaled state of a control flow {@link ControlFlow} once. The control flow can still be signaled: If
     * it was signaled twice and this method was called once, the control flow is still signaled once.
     * 
     * @param controlFlow
     *            the {@link ControlFlow}
     */
    void removeSignaledControlFlow(ControlFlow controlFlow);

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
    @JsonProperty
    Map<String, Object> getVariableMap();

    /**
     * Sets a variable for a node that might be required by basically any token that belongs to the.
     * 
     * @param node
     *            the node
     * @param name
     *            the name
     * @param value
     *            the value {@link AbstractProcessInstance} to perform process execution.
     */
    void setNodeVariable(Node node, String name, Object value);

    /**
     * Gets a variable that has been defined for this node in the current context.
     * 
     * @param node
     *            the node
     * @param name
     *            the name
     * @return the internal variable or <code>null</code>, if the variable was not previously set
     */
    Object getNodeVariable(Node node, String name);

}
