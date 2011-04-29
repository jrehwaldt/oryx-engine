package de.hpi.oryxengine.process.definition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeBuilder;
import de.hpi.oryxengine.process.structure.NodeBuilderImpl;
import de.hpi.oryxengine.process.structure.StartNodeBuilderImpl;
import de.hpi.oryxengine.process.structure.TransitionBuilder;
import de.hpi.oryxengine.process.structure.TransitionBuilderImpl;

/**
 * The Class ProcessBuilderImpl. As you would think, only nodes that were created using createStartNode() become
 * actually start nodes.
 */
public class ProcessBuilderImpl implements ProcessDefinitionBuilder {

    private ProcessDefinition definition;

    private List<Node> startNodes;

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
        this.startNodes = new ArrayList<Node>();
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
    public ProcessDefinitionBuilder createStartTrigger(StartEvent event, Node startNode) {

        this.temporaryStartTriggers.put(event, startNode);
        return this;
    }

    @Override
    public ProcessDefinitionBuilder setAttribute(String attributeId, Object attibuteValue) {

        if (this.temporaryAttributeTable == null) {
            this.temporaryAttributeTable = new HashMap<String, Object>();
        }

        this.temporaryAttributeTable.put(attributeId, attibuteValue);

        return this;
    }

    @Override
    public NodeBuilder getNodeBuilder() {

        return new NodeBuilderImpl();
    }

    @Override
    public TransitionBuilder getTransitionBuilder() {

        return new TransitionBuilderImpl();
    }

    @Override
    public NodeBuilder getStartNodeBuilder() {

        return new StartNodeBuilderImpl(this);
    }

    /**
     * Getter for the StartNodes-List.
     * 
     * @return a {@link List} of {@link Node}
     */
    public List<Node> getStartNodes() {

        return startNodes;
    }
}
