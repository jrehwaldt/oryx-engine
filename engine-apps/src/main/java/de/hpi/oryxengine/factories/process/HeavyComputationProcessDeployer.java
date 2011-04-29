package de.hpi.oryxengine.factories.process;

import de.hpi.oryxengine.node.factory.bpmn.BpmnFunNodeFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnNodeFactory;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;
import de.hpi.oryxengine.process.structure.Node;

/**
 * A factory for creating HeavyComputationProcessToken objects / process instances.
 * 
 * Creates a process isntance with NUMBER_OF_NODES Hashcomputation nodes using the default algorithm. This is used to
 * create a somewhat higher load on the processinstances. The results are stored in the variables hash1 to hash5.
 */
public class HeavyComputationProcessDeployer extends AbstractProcessDeployer {

    /** The Constant NUMBER_OF_NODES. */
    private final static int NUMBER_OF_NODES = 5;

    /** The Constant PASSWORDS. */
    private final static String[] PASSWORDS = { "Hallo", "toor", "278dahka!§-", "muhhhh", "HPI" };

    private Node startNode;

    private Node lastNode;

    /**
     * Instantiates a new heavy computation process token factory.
     */
    public HeavyComputationProcessDeployer() {

        builder = new ProcessDefinitionBuilderImpl();
    }

    /**
     * Initialize this HashComputation nodes of the factory and connects them so we get an actual graph.
     */
    public void initializeNodes() {

        startNode = BpmnFunNodeFactory.createBpmnNullStartNode(builder);

        this.lastNode = startNode;

        for (int i = 0; i < NUMBER_OF_NODES; i++) {

            Node tmpNode = BpmnFunNodeFactory.createBpmnHashComputationNode(builder, "hash", PASSWORDS[i
                % PASSWORDS.length]);

            BpmnNodeFactory.createTransitionFromTo(builder, this.lastNode, tmpNode);
            this.lastNode = tmpNode;
        }

        Node endNode = BpmnNodeFactory.createBpmnEndEventNode(builder);

        BpmnNodeFactory.createTransitionFromTo(builder, this.lastNode, endNode);
    }

}
