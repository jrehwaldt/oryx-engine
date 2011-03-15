package de.hpi.oryxengine.process.definition;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.process.structure.Node;

/**
 * The Class ProcessDefinitionImpl.
 */
public class ProcessDefinitionImpl implements ProcessDefinition {
    
    private String description;
    private UUID id;
    private List<Node> startNodes;
    
    /**
     * Instantiates a new process definition. A UUID is generated randomly.
     *
     * @param id the process definition id
     * @param description the description
     * @param startNodes the initial nodes that refer to the whole node-tree
     */
    public ProcessDefinitionImpl(UUID id, String description, List<Node> startNodes) {
        this.id = id;
        this.description = description;
        this.startNodes = startNodes;
    }

    @Override
    public UUID getID() {

        return id;
    }

    @Override
    public String getDescription() {

        return description;
    }

    @Override
    public void setDescription(String description) {

        this.description = description;
    }
    
    @Override
    public List<Node> getStartNodes() {

        return startNodes;
    }

}
