package de.hpi.oryxengine.factories.process;

import de.hpi.oryxengine.node.factory.bpmn.BpmnCustomNodeFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnNodeFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import de.hpi.oryxengine.process.structure.Node;

/**
 * A factory for creating ExampleProcessToken objects. These objects just have 2 add Number activities.
 */
public class ExampleProcessDeployer extends AbstractProcessDeployer {

    /** The node1. */
    private Node node1;

    /** The node2. */
    private Node node2;

    /** The start node. */
    private Node startNode;


    /**
     * Initializes the nodes.
     */
    public void initializeNodes() {

        startNode = BpmnCustomNodeFactory.createBpmnNullStartNode(processDefinitionBuilder);

        int[] ints = {1, 1};
        node1 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(processDefinitionBuilder, "result", ints);
        node2 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(processDefinitionBuilder, "result", ints);
        
        Node endNode = BpmnNodeFactory.createBpmnEndEventNode(processDefinitionBuilder);
        
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, startNode, node1);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, node1, node2);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, node2, endNode);
        
        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(processDefinitionBuilder);
    }

}
