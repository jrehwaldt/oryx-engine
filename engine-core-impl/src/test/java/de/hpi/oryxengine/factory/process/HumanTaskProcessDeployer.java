package de.hpi.oryxengine.factory.process;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.HumanTaskActivity;
import de.hpi.oryxengine.activity.impl.NullActivity;

import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.factory.worklist.TaskFactory;
import de.hpi.oryxengine.process.definition.NodeParameter;
import de.hpi.oryxengine.process.definition.NodeParameterImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilder;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;


/**
 * A factory for creating ExampleProcessToken objects.
 * These objects just have 2 add Number activities.
 */
public class HumanTaskProcessDeployer extends AbstractProcessDeployer {
    
    /** The node1. */
    private Node node1;
    
    /** The node2. */
    private Node node2;
    
    /** The builder. */
    private ProcessBuilder builder;
    
    /** The start node. */
    private Node startNode;
    
    private Task task = null;
    
    /**
     * Instantiates a new example process token factory.
     */
    public HumanTaskProcessDeployer() {
        
        builder = new ProcessBuilderImpl();

    }
    
    /**
     * Initializes the nodes.
     */
    public void initializeNodes() {
        NodeParameter param = new NodeParameterImpl(new NullActivity(), new SimpleJoinBehaviour(),
            new TakeAllSplitBehaviour());
        param.makeStartNode(true);
        startNode = builder.createNode(param);
        param.makeStartNode(false);
        
        // Create the task
        task = TaskFactory.createJannikServesGerardoTask();
        Activity activity  = new HumanTaskActivity(task);
        param.setActivity(activity);
        
        node1 = builder.createNode(param);
        node2 = builder.createNode(param);
        builder.createTransition(startNode, node1).createTransition(node1, node2);
        
        param.setActivity(new EndActivity());
        Node endNode = builder.createNode(param);
        builder.createTransition(node2, endNode);

    }

    /**
     * {@inheritDoc}
     */
    public void createPseudoHuman() {
        PseudoHumanThread thread = new PseudoHumanThread("ein name");
        thread.start();
    }

}
