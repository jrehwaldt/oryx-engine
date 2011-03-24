package de.hpi.oryxengine.process.definition;

import java.util.UUID;

import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.StartNode;

/**
 * The Interface ProcessBuilder. The process builder is a comfortable way to construct a process definition.
 * @author thorben
 */
public interface ProcessBuilder {

    /**
     * Gets the definition as the result of the building process.
     *
     * @return the definition
     */
    ProcessDefinition buildDefinition();
    
    /**
     * Creates a new node with the given parameters.
     *
     * @param param the param
     * @return the node
     */
    Node createNode(NodeParameter param);
    
    StartNode createStartNode(StartNodeParameter param);
    
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
}
