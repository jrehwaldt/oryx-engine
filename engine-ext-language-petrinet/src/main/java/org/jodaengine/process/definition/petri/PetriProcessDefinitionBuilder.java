package org.jodaengine.process.definition.petri;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.TransitionBuilder;
import org.jodaengine.process.structure.TransitionBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class PetriProcessDefinitionBuilder.
 */
public class PetriProcessDefinitionBuilder {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private List<Node> startNodes;
    private ProcessDefinitionID id;
    private String name;
    private String description;
    
    public String getDescription() {
    
        return description;
    }

    public PetriProcessDefinitionBuilder setStartNodes(List<Node> startNodes) {
    
        this.startNodes = startNodes;
        return this;
    }

    public static PetriProcessDefinitionBuilder newBuilder() {
        return new PetriProcessDefinitionBuilder();
    }

    public PetriProcessDefinitionBuilder setName(String processName) {

        this.name = processName;
        this.id = new ProcessDefinitionID(name);
        return this;
    }

    public PetriProcessDefinitionBuilder setDescription(String description) {

        this.description = description;
        return this;

    }

    public TransitionBuilder getTransitionBuilder() {

        return new TransitionBuilderImpl();
    }

    /**
     * Getter for the StartNodes-List.
     * 
     * @return a {@link List} of {@link Node}
     */
    public List<Node> getStartNodes() {

        return startNodes;
    }


    public ProcessDefinition buildDefinition() {

        PetriNetProcessDefinition definition = new PetriNetProcessDefinition(name, description, id, startNodes);

        // cleanup
        resetingThisBuilder();

        return definition;
    }
    
    public void resetingThisBuilder() {
        this.startNodes = new ArrayList<Node>();

        this.id = new ProcessDefinitionID(UUID.randomUUID().toString());
        this.name = null;
        this.description = null;
    }


}
