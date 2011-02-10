package de.hpi.oryxengine.navigator.impl;

import java.util.ArrayList;
import java.util.HashMap;

import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.node.NodeImpl;
import de.hpi.oryxengine.processDefinitionImpl.AbstractProcessDefinitionImpl;
import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processInstanceImpl.ProcessInstanceImpl;

public class NavigatorImpl implements Navigator {
	
	//map IDs to Definition
	private HashMap<String, ProcessInstanceImpl> runningInstances;
	private HashMap<String, AbstractProcessDefinitionImpl> loadedDefinitions; 

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
		ProcessInstanceImpl processInstance = new ProcessInstanceImpl(loadedDefinitions.get(processID));
		runningInstances.put(processInstance.getID(), processInstance);
		
		// we need to do this, as after node execution (in Navigator#signal() the currentNodes-Datastructure is altered.
		// Its not cool to change the datastructure you iterate over.
		ArrayList<NodeImpl> iterableNodes = (ArrayList<NodeImpl>) processInstance.getCurrentNodes().clone();
		for (NodeImpl node : iterableNodes){
			node.execute(this);
		}
		
		//tell the initial nodes to execute their activities
		
		return "";
	}
	
	
	//this method is for first testing only, as we do not have ProcessDefinitions yet
	public void startArbitraryInstance(String id, ProcessInstanceImpl instance) {
		runningInstances.put(id, instance);
		ArrayList<NodeImpl> iterableNodes = (ArrayList<NodeImpl>) instance.getCurrentNodes().clone();
		for (NodeImpl node : iterableNodes){
			node.execute(this);
		}
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
	
	public void signal(NodeImpl node) {
		ProcessInstance instance = node.getProcessInstance();
		
		ArrayList<NodeImpl> instanceActivities = instance.getCurrentNodes();
		instanceActivities.remove(node);
		
		for (NodeImpl nextNode : node.getNextNodes()) {
			instanceActivities.add(nextNode);
			
			// pass the process instance on. We do not set the process instance for every
			// node in the constructor of the ProcessInstanceImpl as we do not want the Nodes to be instantiated
			// beforehand of the whole execution
			nextNode.setProcessInstance(node.getProcessInstance());
			nextNode.execute(this);
		}		
	}
}
