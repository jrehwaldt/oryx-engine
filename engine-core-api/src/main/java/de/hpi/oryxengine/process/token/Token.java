package de.hpi.oryxengine.process.token;

import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.process.instance.ProcessInstanceContext;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;
import de.hpi.oryxengine.util.Identifiable;

/**
 * The Interface ProcessInstance.
 */
public interface Token
extends Identifiable {

    /**
     * Gets the current node.
     * 
     * @return the current node
     */
    Node getCurrentNode();

    /**
     * Sets the current node.
     * 
     * @param node
     *            the new current node
     */
    void setCurrentNode(Node node);

    /**
     * Gets the iD.
     *
     * @return the iD
     * {@inheritDoc}
     */
    @Override
    UUID getID();


    /**
     * Executes a step for the given instance, which is usually a single step beginning with the current node.
     *
     * @return the list of new ProcessInstances that result after the next step is performed.
     * @throws Exception the exception
     */
    void executeStep() throws Exception;

    /**
     * Create a new to navigate instance for every node.
     * Therefore it is possible to use this generic for e.g. and, xor...
     * 
     * @param transitionList a list with redirections
     * @return newly created subprocesses
     */
    List<Token> navigateTo(List<Transition> transitionList);
    
    /**
     * Creates a new token pointing to the given node n in the same process instance context.
     * 
     * @param n the node the new token points to.
     * @return the new process token
     */
    Token createNewToken(Node n);
    
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
    Token performJoin();
    
    /**
     * Gets the context.
     *
     * @return the context
     */
    ProcessInstanceContext getContext();
    
    /**
     * Gets the last taken transition of the token.
     *
     * @return the last taken transition
     */
    Transition getLastTakenTransition();
    
    /**
     * Sets the last taken transition.
     *
     * @param t the new last taken transitions
     */
    void setLastTakenTransition(Transition t);

    /**
     * Stopping the token navigation.
     */
    void suspend();

    /**
     * Continueing the token navigation.
     */
    void resume() throws Exception;
}
