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
public class ProcessBuilderImpl implements ProcessBuilder {

    private ProcessDefinition definition;

    private List<Node> startNodes = new ArrayList<Node>();

    private UUID id;

    private String description;

    private Map<StartEvent, Node> temporaryStartTriggers;

    /**
     * Instantiates some temporary datastructures.
     */
    public ProcessBuilderImpl() {

        this.temporaryStartTriggers = new HashMap<StartEvent, Node>();
    }

    /**
     * {@inheritDoc}
     * @throws IllegalStarteventException 
     */
    @Override
    public ProcessDefinition buildDefinition() throws IllegalStarteventException {

        definition = new ProcessDefinitionImpl(id, description, startNodes);

        for (Map.Entry<StartEvent, Node> entry : temporaryStartTriggers.entrySet()) {
            definition.addStartTrigger(entry.getKey(), entry.getValue());
        }
        
        //cleanup
        startNodes = new ArrayList<Node>();
        id = null;
        description = null;
        this.temporaryStartTriggers = new HashMap<StartEvent, Node>();
        
        return definition;
    }

    @Override
    public Node createNode(NodeParameter param) {

        Node node = new NodeImpl(param.getActivity(), param.getIncomingBehaviour(), param.getOutgoingBehaviour());
        if (param.isStartNode()) {
            startNodes.add(node);
        }
        return node;
    }

    @Override
    public ProcessBuilder createTransition(Node source, Node destination) {

        source.transitionTo(destination);
        return this;
    }

    @Override
    public ProcessBuilder createTransition(Node source, Node destination, Condition condition) {

        source.transitionToWithCondition(destination, condition);
        return this;
    }

    @Override
    public ProcessBuilder setID(UUID id) {

        this.id = id;
        return this;

    }

    @Override
    public ProcessBuilder setDescription(String description) {

        this.description = description;
        return this;

    }

    @Override
    public void createStartTrigger(StartEvent event, Node startNode)
    throws DalmatinaException {

        temporaryStartTriggers.put(event, startNode);

    }

}
