package de.hpi.oryxengine.navigator.impl;

import java.util.ArrayList;

import de.hpi.oryxengine.navigator.NavigatorInterface;
import de.hpi.oryxengine.processDefinitionImpl.ProcessDefinitionImpl;
import de.hpi.oryxengine.processInstance.ProcessInstance;

public class Navigator implements NavigatorInterface {
	
	private ArrayList<ProcessInstance> runningInstances;
	private ArrayList<ProcessDefinitionImpl> loadedDefinitions; 

	public int startProcessInstance(int processID) {
		
		//A.transitionTo(B);
		//B...
		//Process
		return 0;
	}
	
	private Node setNextNode(){
		for (Node node : getFinishedActivities()){
			setNextActivity(node.getNextActvity())
		}
	}

	public void stopProcessInstance(int instanceID) {
		// TODO Auto-generated method stub
		
	}

	public String getCurrentInstanceState(int instanceID) {
		// TODO Auto-generated method stub
		return null;
	}

}
