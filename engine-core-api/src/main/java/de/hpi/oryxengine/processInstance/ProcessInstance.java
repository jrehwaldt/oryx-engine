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
     * Gets the child instances.
     * 
     * @return the child instances
     */
    List<ProcessInstance> getChildInstances();

    /**
     * Sets the child instances.
     * 
     * @param children
     *            the new child instances
     */
    void setChildInstances(List<ProcessInstance> children);

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
     * Executes a step for the given instance, which is usually a single step beginning with the current node.
     * 
     * @return the list of new ProcessInstances that result after the next step is performed.
     */
    List<ProcessInstance> executeStep();

    /**
     * Take all outgoing transitions of the current node. If there is only one outgoing transition, the given
     * ProcessInstance will be moved on. In case of more than one outgoing transitions, child instances are created.
     * 
     * @return the list
     */
    List<ProcessInstance> takeAllTransitions();

    /**
     * Take single transition. The given ProcessInstance will be moved to the destination of the given Transition.
     * 
     * @param t
     *            the transition to take
     * @return a list with a single ProcessInstance in it.
     */
    List<ProcessInstance> takeSingleTransition(Transition t);

    /**
     * Creates a child instance pointing to the given node n.
     * 
     * @param n
     *            the node the new instance points to.
     * @return the new process instance
     */
    ProcessInstance createChildInstance(Node n);

}
