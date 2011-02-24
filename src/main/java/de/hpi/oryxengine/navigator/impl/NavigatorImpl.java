package de.hpi.oryxengine.navigator.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.processDefinitionImpl.AbstractProcessDefinitionImpl;
import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processInstanceImpl.ProcessInstanceImpl;




/**
 * 
 * @author Jannik Streek
 * 
 */

public class NavigatorImpl implements Navigator {

    // map IDs to Definition
    private HashMap<String, ProcessInstanceImpl> runningInstances;
    private HashMap<String, AbstractProcessDefinitionImpl> loadedDefinitions;
    private List<ProcessInstance> toNavigate;
    private ArrayList<NavigationThread> executionThreads;
    private static final int NUMBER_OF_NAVIGATOR_THREADS = 10;

    public NavigatorImpl() {

        // TODO Lazy initialized
        runningInstances = new HashMap<String, ProcessInstanceImpl>();
        loadedDefinitions = new HashMap<String, AbstractProcessDefinitionImpl>();
        toNavigate = new LinkedList<ProcessInstance>();
        toNavigate = Collections.synchronizedList(toNavigate);
        executionThreads = new ArrayList<NavigationThread>();
    }

    public void start() {

        // "Gentlemen, start your engines"
        for (int i = 0; i < NUMBER_OF_NAVIGATOR_THREADS; i++) {
            NavigationThread thread = new NavigationThread("NT" + i, toNavigate);
            thread.start();
            executionThreads.add(thread);
        }
    }

    public String startProcessInstance(String processID) {

        if (!loadedDefinitions.containsKey(processID)) {
            // go crazy
        }

        // instantiate the processDefinition
        ProcessInstanceImpl processInstance = new ProcessInstanceImpl(loadedDefinitions.get(processID), 0);
        runningInstances.put(processInstance.getID(), processInstance);

        // we need to do this, as after node execution (in Navigator#signal() the currentNodes-Datastructure is altered.
        // Its not cool to change the datastructure you iterate over.
        toNavigate.add(processInstance);

        // tell the initial nodes to execute their activities

        return "aProcessInstanceID";
    }

    // this method is for first testing only, as we do not have ProcessDefinitions yet
    public void startArbitraryInstance(String id, ProcessInstanceImpl instance) {

        this.runningInstances.put(id, instance);
        this.toNavigate.add(instance);
    }

    public void addProcessDefinition(AbstractProcessDefinitionImpl processDefinition) {

        loadedDefinitions.put(processDefinition.getID(), processDefinition);
    }

    public void stopProcessInstance(String instanceID) {

        // TODO do some more stuff if instance doesnt exist
        // runningInstances.remove(instanceID);
        // remove from queue...
    }

    public String getCurrentInstanceState(String instanceID) {

        // TODO Auto-generated method stub
        return null;
    }

    public void stop() {

        for (NavigationThread executionThread : executionThreads) {
            executionThread.setShouldStop(true);
        }
    }

}
