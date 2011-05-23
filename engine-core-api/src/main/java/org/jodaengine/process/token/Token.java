package org.jodaengine.process.token;

import java.util.List;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.util.Identifiable;

/**
 * The Interface Token.
 *
 * @param <T> the generic type
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public interface Token<T extends Token<?>> extends Identifiable<UUID> {
    
    /**
     * Gets the current node.
     * 
     * @return the current node
     */
    @JsonProperty
    Node<T> getCurrentNode();

    /**
     * Sets the current node.
     * 
     * @param node
     *            the new current node
     */
    void setCurrentNode(Node<T> node);
    
    /**
     * Executes a step for the given instance, which is usually a single step beginning with the current node.
     * 
     * @throws JodaEngineException
     *             the exception
     */
    void executeStep()
    throws JodaEngineException;

    /**
     * Create a new to navigate instance for every node. Therefore it is possible to use this generic for e.g. and,
     * xor...
     * 
     * @param transitionList
     *            a list with redirections
     * @return newly created subprocesses
     */
    List<T> navigateTo(List<Transition<T>> transitionList);

    /**
     * Creates a new token pointing to the given node n in the same process instance context.
     * 
     * @param n
     *            the node the new token points to.
     * @return the new process token
     */
    T createNewToken(Node<T> n);

    /**
     * Returns a boolean whether it is possible to join on the current node.
     * 
     * @return true, if successful
     */
    boolean joinable();

    /**
     * Perform the join.
     * 
     * @return the token
     */
    T performJoin();

    /**
     * Gets the process instance this token belongs to.
     * 
     * @return the context
     */
    @JsonBackReference
    AbstractProcessInstance<T> getInstance();

    /**
     * Gets the last taken transition of the token.
     * 
     * @return the last taken transition
     */
    @JsonIgnore
    Transition<T> getLastTakenTransition();

    /**
     * Sets the last taken transition.
     * 
     * @param t
     *            the new last taken transitions
     */
    void setLastTakenTransition(Transition<T> t);
    
    /**
     * Gets the navigator that this token is assigned to.
     * 
     * @return the navigator
     */
    @JsonIgnore
    Navigator getNavigator();
    
    /**
     * Cancels the currently ongoing activity.
     */
    void cancelExecution();

}
