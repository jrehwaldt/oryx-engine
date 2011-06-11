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
import org.jodaengine.process.definition.bpmn.BpmnProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeImpl;


/**
 * A factory for creating SimpleProcessDefinition objects.
 */
public class SimpleProcessDefinitionFactory implements ProcessDefinitionFactory {

    private static final String DEFINITION_NAME = "name";
    private static final String DEFINITION_DESCRIPTION = "description";

    @Override
    public ProcessDefinition create(ProcessDefinitionID definitionID) {

        IncomingBehaviour incomingBehaviour = new SimpleJoinBehaviour();
        OutgoingBehaviour outgoingBehaviour = new TakeAllSplitBehaviour();
        Activity activityBehavior = new AddNumbersAndStoreActivity("result", 1, 1);
        Node node1 = new NodeImpl(activityBehavior, incomingBehaviour, outgoingBehaviour);
        
        incomingBehaviour = new SimpleJoinBehaviour();
        outgoingBehaviour = new TakeAllSplitBehaviour();
        activityBehavior = new AddNumbersAndStoreActivity("result", 1, 1);
        Node node2 = new NodeImpl(activityBehavior, incomingBehaviour, outgoingBehaviour);
        
        node1.controlFlowTo(node2);
        List<Node> startNodes = new ArrayList<Node>();
        startNodes.add(node1);
        ProcessDefinition def = new BpmnProcessDefinition(definitionID, DEFINITION_NAME, DEFINITION_DESCRIPTION,
            startNodes);
        return def;
    }

}
