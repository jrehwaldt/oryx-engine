package de.hpi.oryxengine.processInstance;

import java.util.List;

import de.hpi.oryxengine.processstructure.Transition;
import de.hpi.oryxengine.processstructure.impl.NodeImpl;

/**
 * The Interface ProcessInstance.
 */
public interface ProcessInstance {

    /**
     * Gets the current node.
     *
     * @return the current node
     */
    NodeImpl getCurrentNode();

    /**
     * Sets the current node.
     *
     * @param node the new current node
     */
    void setCurrentNode(NodeImpl node);

    /**
     * Gets the iD.
     *
     * @return the iD
     */
    String getID();

    /**
     * Sets the iD.
     *
     * @param s the new iD
     */
    void setID(String s);

    /**
     * Sets the variable.
     *
     * @param name the name
     * @param value the value
     */
    void setVariable(String name, Object value);

    /**
     * Gets the variable.
     *
     * @param name the name
     * @return the variable
     */
    Object getVariable(String name);

    /**
     * Execute step.
     *
     * @return the list
     */
    List<ProcessInstance> executeStep();

    /**
     * Take all transitions.
     */
    List<ProcessInstance> takeAllTransitions();

    /**
     * Take single transition.
     *
     * @param t the transition to take
     */
    List<ProcessInstance> takeSingleTransition(Transition t);

}
