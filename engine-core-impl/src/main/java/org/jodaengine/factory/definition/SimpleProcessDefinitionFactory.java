package org.jodaengine.factory.definition;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.activity.custom.AddNumbersAndStoreActivity;
import org.jodaengine.node.incomingbehaviour.IncomingBehaviour;
import org.jodaengine.node.incomingbehaviour.SimpleJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.OutgoingBehaviour;
import org.jodaengine.node.outgoingbehaviour.TakeAllSplitBehaviour;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.ProcessDefinitionImpl;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeImpl;
import org.jodaengine.process.token.BPMNToken;


/**
 * A factory for creating SimpleProcessDefinition objects.
 */
public class SimpleProcessDefinitionFactory implements ProcessDefinitionFactory {

    private static final String DEFINITION_NAME = "name";
    private static final String DEFINITION_DESCRIPTION = "description";

    @Override
    public ProcessDefinition create(ProcessDefinitionID definitionID) {

        IncomingBehaviour<BPMNToken> incomingBehaviour = new SimpleJoinBehaviour();
        OutgoingBehaviour outgoingBehaviour = new TakeAllSplitBehaviour();
        Activity activityBehavior = new AddNumbersAndStoreActivity("result", 1, 1);
        Node<BPMNToken> node1 = new NodeImpl<BPMNToken>(activityBehavior, incomingBehaviour, outgoingBehaviour);
        
        incomingBehaviour = new SimpleJoinBehaviour();
        outgoingBehaviour = new TakeAllSplitBehaviour();
        activityBehavior = new AddNumbersAndStoreActivity("result", 1, 1);
        Node<BPMNToken> node2 = new NodeImpl<BPMNToken>(activityBehavior, incomingBehaviour, outgoingBehaviour);
        
        node1.transitionTo(node2);
        List<Node<BPMNToken>> startNodes = new ArrayList<Node<BPMNToken>>();
        startNodes.add(node1);
        ProcessDefinition def = new ProcessDefinitionImpl(definitionID, DEFINITION_NAME, DEFINITION_DESCRIPTION,
            startNodes);
        return def;
    }

}
