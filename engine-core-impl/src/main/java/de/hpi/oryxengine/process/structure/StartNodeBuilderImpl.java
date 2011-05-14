package de.hpi.oryxengine.process.structure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;

/**
 * The implementation of the {@link NodeBuilder} for StartNodes. So this class configures and creates a {@link Node
 * StartNode}.
 */
public class StartNodeBuilderImpl extends NodeBuilderImpl {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ProcessDefinitionBuilderImpl processDefinitionBuilder;

    /**
     * Default Constructor.
     * 
     * @param processDefinitionBuilderImpl
     *            - the {@link ProcessDefinitionBuilderImpl} that builds the {@link ProcessDefinition} that should
     *            contain this {@link Node startNode}
     */
    public StartNodeBuilderImpl(ProcessDefinitionBuilderImpl processDefinitionBuilderImpl) {

        this.processDefinitionBuilder = processDefinitionBuilderImpl;
    }

    @Override
    public Node buildNode() {

        checkingNodeConstraints();

        Node resultNode = buildResultNode();

        processStartNode(resultNode);

        return resultNode;
    }

    /**
     * Doing after work in order to register the {@link Node} as startNode.
     * 
     * @param resultNode
     *            - {@link Node} that should be processed
     */
    private void processStartNode(Node resultNode) {

        logger.info("Adding the Node {} to the StartNodeArray.", resultNode);
        processDefinitionBuilder.getStartNodes().add(resultNode);
    }
}
