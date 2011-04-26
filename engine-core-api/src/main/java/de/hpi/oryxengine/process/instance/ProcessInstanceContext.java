package de.hpi.oryxengine.process.instance;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;

/**
 * The Interface ProcessInstanceContext. It is shared between Tokens that work in the same process scope, for example in
 * the same subprocess. This makes it possible to synchronize them at parallel joins and share process instance
 * variables.
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public interface ProcessInstanceContext {

    // TODO @ALLE: -> I think I spider ^^ - Das ist doch nicht euer ernst. Ich verfluche dieses blöde Spiel auf dem
    // Hudson und jAutoDoc. Ganz ehrlich schaut euch doch mal die Kommentare in dieser Klasse an.

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
     * @see de.hpi.oryxengine.process.token.Token#getVariable(java.lang.String)
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
