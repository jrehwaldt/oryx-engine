package de.hpi.oryxengine.process.instance;

import java.util.List;

import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.util.Identifiable;

/**
 * The Interface ProcessInstance that represents what you actually call a process instance. It has several execution
 * threads, represented as tokens and a context that holds variables.
 */
public interface ProcessInstance extends Identifiable {

    /**
     * Assigns the token to this instance.
     * 
     * @param t
     *            the t
     */
    void addToken(Token t);

    /**
     * Removes a token from the instance, for example, if it has finished execution.
     * 
     * @param t
     *            the t
     */
    void removeToken(Token t);

    /**
     * Gets the instance context that is shared among all tokens that belong to this instance.
     * 
     * @return the context
     */
    ProcessInstanceContext getContext();

    /**
     * Gets the tokens that belong to this instance.
     * 
     * @return the tokens
     */
    List<Token> getTokens();

    /**
     * Gets the definition this instance was created for.
     * 
     * @return the definition
     */
    ProcessDefinition getDefinition();

    /**
     * Creates the token referencing the given navigator and places it on the supplied node.
     * 
     * @param node
     *            the node
     * @param nav
     *            the nav
     * @return the token
     */
    Token createToken(Node node, Navigator nav);

    /**
     * Checks if there tokens assigned to this instance. This indicates, that this instance is still running. If this
     * evaluates to false, this does not necessarily meant that this instance has finished, as it might not have been
     * started yet.
     * 
     * @return true, if there are tokens assigned
     */
    boolean hasAssignedTokens();

}
