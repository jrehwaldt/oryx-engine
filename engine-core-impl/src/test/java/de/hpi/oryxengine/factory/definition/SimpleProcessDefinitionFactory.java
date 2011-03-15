package de.hpi.oryxengine.factory.definition;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.factory.node.AddNumbersAndStoreNodeFactory;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionImpl;
import de.hpi.oryxengine.process.structure.Node;

/**
 * A factory for creating SimpleProcessDefinition objects.
 */
public class SimpleProcessDefinitionFactory implements ProcessDefinitionFactory {

    @Override
    public ProcessDefinition create(UUID definitionID) {

        AddNumbersAndStoreNodeFactory factory = new AddNumbersAndStoreNodeFactory();
        Node node1 = factory.create();
        Node node2 = factory.create();
        node1.transitionTo(node2);
        List<Node> startNodes = new ArrayList<Node>();
        startNodes.add(node1);
        ProcessDefinition def = new ProcessDefinitionImpl(definitionID, "description", startNodes);
        return def;
    }

}
