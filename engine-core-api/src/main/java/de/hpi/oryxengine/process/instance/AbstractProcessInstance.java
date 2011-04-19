package de.hpi.oryxengine.process.instance;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;

import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.util.Identifiable;

/**
 * The Interface ProcessInstance that represents what you actually call a process instance. It has several execution
 * threads, represented as tokens and a context that holds variables.
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public abstract class AbstractProcessInstance implements Identifiable {

    /**
     * Assigns the token to this instance.
     * 
     * @param t
     *            the t
     */
    public abstract void addToken(Token t);

    /**
     * Removes a token from the instance, for example, if it has finished execution.
     * 
     * @param t
     *            the t
     */
    public abstract void removeToken(Token t);

    /**
     * Gets the instance context that is shared among all tokens that belong to this instance.
     * 
     * @return the context
     */
    @JsonProperty
    public abstract ProcessInstanceContext getContext();

    /**
     * Gets the tokens that belong to this instance.
     * 
     * @return the tokens
     */
    @JsonProperty
    public abstract List<Token> getAssignedTokens();

    /**
     * Gets the definition this instance was created for.
     * 
     * @return the definition
     */
    @JsonProperty
    public abstract ProcessDefinition getDefinition();

    /**
     * Creates the token referencing the given navigator and places it on the supplied node.
     * 
     * @param node
     *            the node
     * @param nav
     *            the nav
     * @return the token
     */
    public abstract Token createToken(Node node, Navigator nav);

    /**
     * Checks if there tokens assigned to this instance. This indicates, that this instance is still running. If this
     * evaluates to false, this does not necessarily meant that this instance has finished, as it might not have been
     * started yet.
     * 
     * @return true, if there are tokens assigned
     */
    public abstract boolean hasAssignedTokens();
    
    /**
     * Cancel the execution of all tokens belonging to this instance.
     */
    public abstract void cancel();
    
    /**
     * Checks if this instance was cancelled.
     *
     * @return true, if is cancelled
     */
    @JsonProperty
    public abstract boolean isCancelled();

}
