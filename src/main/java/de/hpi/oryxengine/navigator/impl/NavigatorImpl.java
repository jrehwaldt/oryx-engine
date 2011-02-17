package de.hpi.oryxengine.navigator.impl;

import java.util.ArrayList;
import java.util.HashMap;
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
	private Queue<ProcessInstance> toNavigate;

	public NavigatorImpl() {
		runningInstances = new HashMap<String, ProcessInstanceImpl>();
		loadedDefinitions = new HashMap<String, AbstractProcessDefinitionImpl>();
	}
	
	public String startProcessInstance(String processID) {
		//A.transitionTo(B);
		//B...
		//Process
		if (!loadedDefinitions.containsKey(processID)) {
			//go crazy
		}
		
		//instantiate the processDefinition
		ProcessInstanceImpl processInstance = new ProcessInstanceImpl(loadedDefinitions.get(processID), 0);
		runningInstances.put(processInstance.getID(), processInstance);
		
		// we need to do this, as after node execution (in Navigator#signal() the currentNodes-Datastructure is altered.
		// Its not cool to change the datastructure you iterate over.
		toNavigate.add(processInstance);
		processInstance.getCurrentNode().execute(processInstance);
		doWork();
		
		//tell the initial nodes to execute their activities
		
		return "";
	}
	
	
	//this method is for first testing only, as we do not have ProcessDefinitions yet
	public void startArbitraryInstance(String id, ProcessInstanceImpl instance) {
		runningInstances.put(id, instance);
		toNavigate.add(instance);
		instance.getCurrentNode().execute(instance);
	}
	
	/*
	private Node setNextNode(){
		for (Node node : getFinishedActivities()){
			setNextActivity(node.getNextActvity())
		}
	}
	*/
	
	public void addProcessDefinition(AbstractProcessDefinitionImpl processDefinition){
		loadedDefinitions.put(processDefinition.getID(), processDefinition);
	}

	public void stopProcessInstance(String instanceID) {
		// TODO do some more stuff if instance doesnt exist
		runningInstances.remove(instanceID);		
	}

	public String getCurrentInstanceState(String instanceID) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void doWork() {
		
		if(toNavigate.size() > 0){
			
			ProcessInstance instance = toNavigate.remove();
			
			NodeImpl instanceActivity = instance.getCurrentNode();
			
			ArrayList<Transition> transitions = instanceActivity.getTransitions();
			
			for(Transition transition : transitions){
				if (transition.getCondition().evaluate()){
					NodeImpl destination = transition.getDestination();
					destination.execute(instance);
					toNavigate.add(instance);
				}
			}
			
		}else{
			
			//Busy Waiting
			doWork();
			
		}

		


	}
}
