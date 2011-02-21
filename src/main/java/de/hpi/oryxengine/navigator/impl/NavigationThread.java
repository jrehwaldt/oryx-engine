package de.hpi.oryxengine.navigator.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processstructure.Transition;
import de.hpi.oryxengine.processstructure.impl.NodeImpl;

public class NavigationThread extends Thread{

	private List<ProcessInstance> toNavigate;
	private Logger logger = Logger.getRootLogger();
	
	
	public NavigationThread(String threadname, List<ProcessInstance> activityQueue){
		super(threadname);
		this.toNavigate = activityQueue;
	}
	public void run(){
		doWork();
	}
	
	// Main Loop: Takes a executable process instance and the belonging node
	// and executes the node. After, the true conditions are followed and the next node is set.
	// Now the process instance is added to the Queue again. This has the advantage that the navigator can now
	// handle multiple instances.
	
	public void doWork() {
		
		while(true){
			if (this.toNavigate.size() > 0) {
				ProcessInstance instance = this.toNavigate.remove(0);
				NodeImpl currentNode = instance.getCurrentNode();
				currentNode.execute(instance);
				ArrayList<Transition> transitions = currentNode.getTransitions();
				
				// TODO [Gerardo] das darf nicht hier hin, denn das sollte von den NodeActivities gekapselt,
				// denn die wissen dach, was sie mit den Transitionen machen sollten
				for(Transition transition : transitions){
					if (transition.getCondition().evaluate()){
						NodeImpl destination = transition.getDestination();
						instance.setCurrentNode(destination);
						
						//TODO: if following transitions there...
						this.toNavigate.add(instance);
					}
				}
			}
			else {
				try {
					logger.debug("Queue empty");
					sleep(1000);					
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
