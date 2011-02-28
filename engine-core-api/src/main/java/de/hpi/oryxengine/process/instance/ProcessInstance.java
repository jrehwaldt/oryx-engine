package de.hpi.oryxengine.process.instance;

import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.util.Identifiable;

/**
 * The Interface ProcessInstance.
 */
public interface ProcessInstance
extends Identifiable {

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
     * {@inheritDoc}
     */
    @Override
    UUID getID();

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
     * Create a new to navigate instance for every node.
     * Therefore it is possible to use this generic for e.g. and, xor...
     * 
     * @param nodeList a list with redirections
     * @return newly created subprocesses
     */
    List<ProcessInstance> navigateTo(List<Node> nodeList)
    throws Exception;
    
    /**
     * Creates a child instance pointing to the given node n.
     * 
     * @param n the node the new instance points to.
     * @return the new process instance
     */
    ProcessInstance createChildInstance(Node n);

}
