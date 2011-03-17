package de.hpi.oryxengine.process.definition;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;

/**
 * The Class ProcessBuilderImpl.
 * 
 * @author thorben
 */
public class ProcessBuilderImpl implements ProcessBuilder {

    /** The definition. */
    private ProcessDefinition definition;

    /** The start nodes. */
    private List<Node> startNodes = new ArrayList<Node>();

    /** The id. */
    private UUID id;

    /** The description. */
    private String description;

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessDefinition buildDefinition() {

        definition = new ProcessDefinitionImpl(id, description, startNodes);
        startNodes = new ArrayList<Node>();
        return definition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node createNode(NodeParameter param) {

        Node node = new NodeImpl(param.getActivity(), param.getIncomingBehaviour(), param.getOutgoingBehaviour());
        if (param.isStartNode()) {
            startNodes.add(node);
        }
        return node;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessBuilder createTransition(Node source, Node destination) {

        source.transitionTo(destination);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessBuilder createTransition(Node source, Node destination, Condition condition) {

        source.transitionToWithCondition(destination, condition);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setID(UUID id) {

        this.id = id;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDescription(String description) {

        this.description = description;

    }

}
