package de.hpi.oryxengine.navigator.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.node.NodeImpl;
import de.hpi.oryxengine.processDefinitionImpl.AbstractProcessDefinitionImpl;
import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processInstanceImpl.ProcessInstanceImpl;
import de.hpi.oryxengine.transition.Transition;

public class NavigatorImpl implements Navigator {
	
	//map IDs to Definition
	private HashMap<String, ProcessInstanceImpl> runningInstances;
	private HashMap<String, AbstractProcessDefinitionImpl> loadedDefinitions; 
	private LinkedList<ProcessInstance> toNavigate;

	public NavigatorImpl() {
		runningInstances = new HashMap<String, ProcessInstanceImpl>();
		loadedDefinitions = new HashMap<String, AbstractProcessDefinitionImpl>();
		toNavigate = new LinkedList<ProcessInstance>();
	}
	
	public String startProcessInstance(String processID) {
		if (!loadedDefinitions.containsKey(processID)) {
			//go crazy
		}
		
		//instantiate the processDefinition
		ProcessInstanceImpl processInstance = new ProcessInstanceImpl(loadedDefinitions.get(processID), 0);
		runningInstances.put(processInstance.getID(), processInstance);
		
		// we need to do this, as after node execution (in Navigator#signal() the currentNodes-Datastructure is altered.
		// Its not cool to change the datastructure you iterate over.
		toNavigate.add(processInstance);
		doWork();
		
		//tell the initial nodes to execute their activities
		
		return "";
	}
	
	
	//this method is for first testing only, as we do not have ProcessDefinitions yet
	public void startArbitraryInstance(String id, ProcessInstanceImpl instance) {
		this.runningInstances.put(id, instance);
		this.toNavigate.add(instance);
		doWork();
	}
	
	
	public void addProcessDefinition(AbstractProcessDefinitionImpl processDefinition){
		loadedDefinitions.put(processDefinition.getID(), processDefinition);
	}

	public void stopProcessInstance(String instanceID) {
		// TODO do some more stuff if instance doesnt exist
		//runningInstances.remove(instanceID);		
		//remove from queue...
	}

	public String getCurrentInstanceState(String instanceID) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void doWork() {
		if(this.toNavigate.size() > 0){
			
			ProcessInstance instance = this.toNavigate.remove();
			NodeImpl currentNode = instance.getCurrentNode();
			currentNode.execute(instance);
			ArrayList<Transition> transitions = currentNode.getTransitions();
			
			for(Transition transition : transitions){
				if (transition.getCondition().evaluate()){
					NodeImpl destination = transition.getDestination();
					instance.setCurrentNode(destination);
					this.toNavigate.add(instance);
				}
			}
			
			doWork();
			
		}else{
			
			//TODO: Perhaps later Busy Waiting here, to execute instances in an infinite loop
			//doWork();
			
		}

		


	}
}
