package de.hpi.oryxengine.example;

import java.util.ArrayList;

import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;
import de.hpi.oryxengine.navigator.impl.NavigatorImpl;
import de.hpi.oryxengine.processInstanceImpl.ProcessInstanceImpl;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.impl.NodeImpl;

public class SimpleExampleProcess {

	private static int instanceCount = 100;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NavigatorImpl navigator = new NavigatorImpl();
		navigator.start();
		
		//let's generate some load :)
		for (int i = 0; i < instanceCount; i++){
			ProcessInstanceImpl instance = sampleProcessInstance(i);
			
			System.out.println("Navigator started");
			navigator.startArbitraryInstance("1", instance);
			System.out.println("Instance started");
		}		
	}
	
	private static ProcessInstanceImpl sampleProcessInstance(int counter) {
		
		AutomatedDummyActivity activity = new AutomatedDummyActivity("I suck " + counter);
		AutomatedDummyActivity activity2 = new AutomatedDummyActivity("I suck of course "+ counter);
		
		NodeImpl startNode = new NodeImpl(activity);
		NodeImpl secondNode = new NodeImpl(activity2);
		startNode.setId("1");
		secondNode.setId("2");
		startNode.transitionTo(secondNode);
		ArrayList<Node> startNodes = new ArrayList<Node>();
		startNodes.add(startNode);
		
		ProcessInstanceImpl sampleInstance = new ProcessInstanceImpl(startNodes);
		return sampleInstance;
	}

}
