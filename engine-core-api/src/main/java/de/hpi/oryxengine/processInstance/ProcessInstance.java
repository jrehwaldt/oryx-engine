package de.hpi.oryxengine.processInstance;

import java.util.List;

import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.Transition;

/**
 * The Interface ProcessInstance.
 */
public interface ProcessInstance {

    /**
     * Gets the parent instance.
     * 
     * @return the parent instance
     */
    ProcessInstance getParentInstance();

    /**
     * Sets the parent instance.
     * 
     * @param instance
     *            the new parent instance
     */
    void setParentInstance(ProcessInstance instance);

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
     */
    String getID();

    /**
     * Sets the iD.
     * 
     * @param s
     *            the new iD
     */
    void setID(String s);

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
     * 
     * @return the list
     */
    List<ProcessInstance> takeAllTransitions();

    /**
     * Take single transition.
     * 
     * @param t
     *            the transition to take
     * @return the list
     */
    List<ProcessInstance> takeSingleTransition(Transition t);
    
    /**
     * Creates a child instance pointing to the given node n.
     *
     * @param n the node the new instance points to.
     * @return the new process instance
     */
    ProcessInstance createChildInstance(Node n);

}
