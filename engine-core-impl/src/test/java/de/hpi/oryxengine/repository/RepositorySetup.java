package de.hpi.oryxengine.repository;


import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.factory.node.AddNumbersAndStoreNodeFactory;
import de.hpi.oryxengine.process.definition.NodeParameter;
import de.hpi.oryxengine.process.definition.NodeParameterImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilder;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class RepositorySetup.
 */
public class RepositorySetup {
    
    /** The Constant FIRST_EXAMPLE_PROCESS_ID. */
    public static final UUID FIRST_EXAMPLE_PROCESS_ID = UUID.randomUUID();

    /**
     * Fill repository.
     */
    public static void fillRepository() {
        ProcessRepository repo = ProcessRepositoryImpl.getInstance();
        repo.addDefinition(exampleProcess1());
    }
    
    /**
     * Example process1.
     *
     * @return the process definition
     */
    private static ProcessDefinition exampleProcess1() {
        ProcessBuilder builder = new ProcessBuilderImpl();
        NodeParameter param = new NodeParameterImpl(new AddNumbersAndStoreActivity("result", 1, 1), new SimpleJoinBehaviour(),
            new TakeAllSplitBehaviour());
        
        param.setStartNode(true);
        Node node1 = builder.createNode(param);
        param.setStartNode(false);
        Node node2 = builder.createNode(param);
        builder.createTransition(node1, node2).setDescription("").setID(FIRST_EXAMPLE_PROCESS_ID);
        return builder.buildDefinition();
    }
}
