package de.hpi.oryxengine.navigator.impl;

import java.util.ArrayList;
import java.util.HashMap;

import de.hpi.oryxengine.navigator.NavigatorInterface;
import de.hpi.oryxengine.processDefinitionImpl.AbstractProcessDefinitionImpl;
import de.hpi.oryxengine.processDefinitionImpl.ProcessDefinitionImpl;
import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processInstanceImpl.ProcessInstanceImpl;

public class Navigator implements NavigatorInterface {
	
	//map IDs to Definition
	private HashMap<String, ProcessInstanceImpl> runningInstances;
	private HashMap<String, AbstractProcessDefinitionImpl> loadedDefinitions; 

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
		
		//tell the initial nodes to execute their activities
		
		return "";
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

}
