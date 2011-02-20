package de.hpi.oryxengine.example;

import java.util.ArrayList;

import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;
import de.hpi.oryxengine.navigator.impl.NavigatorImpl;
import de.hpi.oryxengine.node.Node;
import de.hpi.oryxengine.node.NodeImpl;
import de.hpi.oryxengine.processInstanceImpl.ProcessInstanceImpl;

public class SimpleExampleProcess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NavigatorImpl navigator = new NavigatorImpl();
		ProcessInstanceImpl instance = sampleProcessInstance();
		navigator.startArbitraryInstance("1", instance);
		

	}
	
	private static ProcessInstanceImpl sampleProcessInstance() {
		
		AutomatedDummyActivity activity = new AutomatedDummyActivity("I suck");
		AutomatedDummyActivity activity2 = new AutomatedDummyActivity("I suck of course");
		
		NodeImpl startNode = new NodeImpl(activity);
		NodeImpl secondNode = new NodeImpl(activity2);
		startNode.transitionTo(secondNode);
		ArrayList<Node> startNodes = new ArrayList<Node>();
		startNodes.add(startNode);
		
		ProcessInstanceImpl sampleInstance = new ProcessInstanceImpl(startNodes);
		return sampleInstance;
	}

}
