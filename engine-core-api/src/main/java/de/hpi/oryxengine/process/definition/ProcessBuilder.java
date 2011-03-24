package de.hpi.oryxengine.process.definition;

import java.util.UUID;

import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.Node;

/**
 * The Interface ProcessBuilder. The process builder is a comfortable way to construct a process definition.
 * @author thorben
 */
public interface ProcessBuilder {

    /**
     * Gets the definition as the result of the building process.
     *
     * @return the definition
     * @throws DalmatinaException 
     */
    ProcessDefinition buildDefinition() throws DalmatinaException;
    
    /**
     * Creates a new node with the given parameters.
     *
     * @param param the param
     * @return the node
     */
    Node createNode(NodeParameter param);
    
    /**
     * Creates the transition.
     *
     * @param source the source
     * @param destination the destination
     * @return the process builder
     */
    ProcessBuilder createTransition(Node source, Node destination);
    
    /**
     * Creates the transition.
     *
     * @param source the source
     * @param destination the destination
     * @param condition the condition
     * @return the process builder
     */
    ProcessBuilder createTransition(Node source, Node destination, Condition condition);
    
    /**
     * Sets the iD.
     *
     * @param id the new ID
     * @return the process builder
     */
    ProcessBuilder setID(UUID id);
    
    /**
     * Sets the description.
     *
     * @param description the new description
     * @return the process builder
     */
    ProcessBuilder setDescription(String description);
    
    /**
     * This will create a start trigger for the process definition.
     *
     * @param event the event
     * @param startNode the start node
     * @throws DalmatinaException thrown if the provided node isn't a startNode.
     */
    void createStartTrigger(StartEvent event, Node startNode) throws DalmatinaException;
}
