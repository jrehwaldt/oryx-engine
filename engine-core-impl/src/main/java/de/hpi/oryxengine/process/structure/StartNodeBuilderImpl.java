package de.hpi.oryxengine.process.structure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeBuilder;

/**
 * The implementation of the {@link NodeBuilder} for StartNodes. So this class configures and creates a {@link Node
 * StartNode}.
 */
public class StartNodeBuilderImpl extends NodeBuilderImpl {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ProcessBuilderImpl processDefinitionBuilder;

    /**
     * Default Constructor.
     * 
     * @param processDefinitionBuilderImpl - a {@link ProcessBuilderImpl}
     */
    public StartNodeBuilderImpl(ProcessBuilderImpl processDefinitionBuilderImpl) {

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
     * Doing afterwork in order to register the {@link Node} as startNode.
     * 
     * @param resultNode
     *            - {@link Node} that should be processed
     */
    private void processStartNode(Node resultNode) {

        logger.info("Adding the Node {} to the StartNodeArray.", resultNode);
        processDefinitionBuilder.getStartNodes().add(resultNode);
    }
}
