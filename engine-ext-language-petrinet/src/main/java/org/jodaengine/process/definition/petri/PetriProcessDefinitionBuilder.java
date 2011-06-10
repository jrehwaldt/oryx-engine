package org.jodaengine.process.definition.petri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.ControlFlowBuilder;
import org.jodaengine.process.structure.ControlFlowBuilderImpl;
import org.jodaengine.util.Attributable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class PetriProcessDefinitionBuilder.
 */
public class PetriProcessDefinitionBuilder implements Attributable {
    

    private List<Node> startNodes;
    private ProcessDefinitionID id;
    private String name;
    private String description;
    private Map<String, Object> attributes;
    
    public String getDescription() {
    
        return description;
    }

    public PetriProcessDefinitionBuilder setStartNodes(List<Node> startNodes) {
    
        this.startNodes = startNodes;
        return this;
    }
    
    public void addStartNode(Node node) {
        if(startNodes == null) {
            startNodes = new ArrayList<Node>();
        }
        startNodes.add(node);
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

    public ControlFlowBuilder getControlFlowBuilder() {

        return new ControlFlowBuilderImpl();
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

        PetriNetProcessDefinition definition = new PetriNetProcessDefinition(id, name, description, startNodes);

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

    @Override
    public Object getAttribute(String attributeKey) {
        return getAttributes().get(attributeKey);
    }
    
    @Override
    public void setAttribute(String attributeKey, Object attributeValue) {
        getAttributes().put(attributeKey, attributeValue);
    }
    
    @Override
    public Map<String, Object> getAttributes() {

        if (this.attributes == null) {
            this.attributes = new HashMap<String, Object>();
        }
        return this.attributes;
    }


}
