package de.hpi.oryxengine.factory.definition;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionImpl;
import de.hpi.oryxengine.process.structure.ActivityBlueprint;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.IncomingBehaviour;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.OutgoingBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * A factory for creating SimpleProcessDefinition objects.
 */
public class SimpleProcessDefinitionFactory implements ProcessDefinitionFactory {

    @Override
    public ProcessDefinition create(UUID definitionID) {

        IncomingBehaviour incomingBehaviour = new SimpleJoinBehaviour();
        OutgoingBehaviour outgoingBehaviour = new TakeAllSplitBehaviour();
        Class<?>[] conSig = {String.class, int[].class};
        int[] integers = {1, 1};
        Object[] conArgs = {"result", integers};
        ActivityBlueprint blueprint = new ActivityBlueprintImpl(AddNumbersAndStoreActivity.class, conSig, conArgs);
        Node node1 = new NodeImpl(blueprint, incomingBehaviour, outgoingBehaviour);
        
        incomingBehaviour = new SimpleJoinBehaviour();
        outgoingBehaviour = new TakeAllSplitBehaviour(); 
        Node node2 = new NodeImpl(blueprint, incomingBehaviour, outgoingBehaviour);
        
        node1.transitionTo(node2);
        List<Node> startNodes = new ArrayList<Node>();
        startNodes.add(node1);
        ProcessDefinition def = new ProcessDefinitionImpl(definitionID, "description", startNodes);
        return def;
    }

}
