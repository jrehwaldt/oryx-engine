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
     * {@inheritDoc}
     */
    @Override
    UUID getID();


    /**
     * Executes a step for the given instance, which is usually a single step beginning with the current node.
     * 
     * @return the list of new ProcessInstances that result after the next step is performed.
     */
    List<Token> executeStep();

    /**
     * Create a new to navigate instance for every node.
     * Therefore it is possible to use this generic for e.g. and, xor...
     * 
     * @param nodeList a list with redirections
     * @return newly created subprocesses
     * @throws Exception if the node to navigate to doesn't exist
     */
    List<Token> navigateTo(List<Transition> transitionList)
    throws Exception;
    
    /**
     * Creates a new token pointing to the given node n in the same processinstancecontext.
     * 
     * @param n the node the new token points to.
     * @return the new process token
     */
    Token createNewToken(Node n);
    
    boolean joinable();
    
    Token performJoin();
    
    ProcessInstanceContext getContext();
    
    Transition getLastTakenTransition();
    
    void setLastTakenTransitions(Transition t);

}
