package de.hpi.oryxengine.process.definition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;

/**
 * The Class ProcessBuilderImpl. As you would think, only nodes that were created using createStartNode() become
 * actually start nodes.
 * 
 * @author thorben
 */
public class ProcessBuilderImpl implements ProcessDefinitionBuilder {

    private ProcessDefinition definition;

    private List<Node> startNodes = new ArrayList<Node>();

    private UUID id;

    private String name;

    private String description;

    private Map<StartEvent, Node> temporaryStartTriggers;
    
    private Map<String, Object> temporaryAttributeTable;

    /**
     * Instantiates some temporary datastructures.
     */
    public ProcessBuilderImpl() {

        this.temporaryStartTriggers = new HashMap<StartEvent, Node>();
        this.id = UUID.randomUUID();
    }

    @Override
    public ProcessDefinition buildDefinition()
    throws IllegalStarteventException {

        this.definition = new ProcessDefinitionImpl(id, name, description, startNodes);

        for (Map.Entry<StartEvent, Node> entry : temporaryStartTriggers.entrySet()) {
            this.definition.addStartTrigger(entry.getKey(), entry.getValue());
        }

        // cleanup
        this.startNodes = new ArrayList<Node>();
        this.id = UUID.randomUUID();
        this.description = null;
        this.temporaryStartTriggers = new HashMap<StartEvent, Node>();
        this.temporaryAttributeTable = null;

        return definition;
    }

    @Override
    public Node createNode(NodeParameter param) {

        Node node = new NodeImpl(param.getActivityBlueprint(), param.getIncomingBehaviour(),
            param.getOutgoingBehaviour());
        return node;
    }

    @Override
    public Node createStartNode(NodeParameter param) {

        Node node = createNode(param);
        this.startNodes.add(node);
        return node;
    }

    @Override
    public ProcessDefinitionBuilder createTransition(Node source, Node destination) {

        source.transitionTo(destination);
        return this;
    }

    @Override
    public ProcessDefinitionBuilder createTransition(Node source, Node destination, Condition condition) {

        source.transitionToWithCondition(destination, condition);
        return this;
    }

//    @Override
//    public ProcessBuilder setID(UUID id) {
//
//        this.id = id;
//        return this;
//
//    }

    @Override
    public ProcessDefinitionBuilder setName(String processName) {
        
        this.name = processName;
        return this;
    }

    @Override
    public ProcessDefinitionBuilder setDescription(String description) {

        this.description = description;
        return this;

    }

    @Override
    public void createStartTrigger(StartEvent event, Node startNode)
    throws DalmatinaException {

        this.temporaryStartTriggers.put(event, startNode);

    }

    @Override
    public ProcessDefinitionBuilder setAttribute(String attributeId, Object attibuteValue) {

        if (this.temporaryAttributeTable == null) {
            this.temporaryAttributeTable = new HashMap<String, Object>();
        }
        
        this.temporaryAttributeTable.put(attributeId, attibuteValue);
        
        return this;
    }

}
