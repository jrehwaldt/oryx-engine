package de.hpi.oryxengine.process.definition;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.process.structure.Node;

/**
 * The implementation of the {@link NodeBuilder} for StartNodes. So this class configures and creates a {@link Node
 * StartNode}.
 */
public class StartNodeBuilderImpl extends NodeBuilderImpl {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private List<Node> startNodes;

    
    public StartNodeBuilderImpl(List<Node> startNodes) {

        this.startNodes = startNodes;
    }

    @Override
    public Node buildNode() {

        Node resultNode = getResultNode();

        processStartNode(resultNode);

        return resultNode;
    }

    private void processStartNode(Node resultNode) {

        logger.info("Adding the Node {} to the StartNodeArray.", resultNode);
        startNodes.add(resultNode);
    }
}
