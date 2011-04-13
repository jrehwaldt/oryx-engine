package de.hpi.oryxengine.factories.process;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.HashComputationActivity;
import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.process.definition.NodeParameter;
import de.hpi.oryxengine.process.definition.NodeParameterImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * A factory for creating HeavyComputationProcessToken objects / process instances.
 * 
 * Creates a process isntance with NUMBER_OF_NODES Hashcomputation nodes using the default algorithm.
 * This is used to create a somewhat higher load on the processinstances.
 * The results are stored in the variables hash1 to hash5.
 */
public class HeavyComputationProcessDeployer extends AbstractProcessDeployer {
    
    /** The Constant NUMBER_OF_NODES. */
    private final static int NUMBER_OF_NODES = 5;
    
    /** The Constant PASSWORDS. */
    private final static String[] PASSWORDS = {"Hallo", "toor", "278dahka!§-", "muhhhh", "HPI"};
    
    private Node startNode;
    
    private Node lastNode;

    /**
     * Instantiates a new heavy computation process token factory.
     */
    public HeavyComputationProcessDeployer() {

        builder = new ProcessBuilderImpl();
    }

    /**
     * Initialize this HashComputation nodes of the factory and connects them so we get an actual graph.
     */
    public void initializeNodes() {
       
        NodeParameter param = new NodeParameterImpl(new NullActivity(), new SimpleJoinBehaviour(),
            new TakeAllSplitBehaviour());
        param.makeStartNode(true);
        startNode = builder.createNode(param);
        param.makeStartNode(false);
        this.lastNode = startNode;
        
        for (int i = 0; i < NUMBER_OF_NODES; i++) {
            Activity activity  = new HashComputationActivity("hash" + String.valueOf(i + 1),
                PASSWORDS[i % PASSWORDS.length], "SHA-512");
            param.setActivity(activity);
            Node tmpNode = builder.createNode(param);
            builder.createTransition(this.lastNode, tmpNode);
            this.lastNode = tmpNode;
        }
        
        param.setActivity(new EndActivity());
        Node endNode = builder.createNode(param);
        builder.createTransition(this.lastNode, endNode);
        
    }


}
