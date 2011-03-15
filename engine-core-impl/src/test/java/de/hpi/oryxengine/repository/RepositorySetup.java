package de.hpi.oryxengine.repository;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.factory.node.AddNumbersAndStoreNodeFactory;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionImpl;
import de.hpi.oryxengine.process.structure.Node;

/**
 * The Class RepositorySetup.
 */
public class RepositorySetup {
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
        AddNumbersAndStoreNodeFactory factory = new AddNumbersAndStoreNodeFactory();
        Node node1 = factory.create();
        Node node2 = factory.create();
        node1.transitionTo(node2);
        List<Node> startNodes = new ArrayList<Node>();
        startNodes.add(node1);
        return new ProcessDefinitionImpl(FIRST_EXAMPLE_PROCESS_ID, "", startNodes);
    }
}
