package de.hpi.oryxengine.processInstanceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hpi.oryxengine.processDefinitionImpl.AbstractProcessDefinitionImpl;
import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.impl.NodeImpl;

public class ProcessInstanceImpl implements ProcessInstance {

    private String id;
    NodeImpl currentNode;
    private ArrayList<ProcessInstanceImpl> childInstances;
    private Map<String, Object> instanceVariables;

    public ProcessInstanceImpl(AbstractProcessDefinitionImpl processDefinition, Integer startNumber) {

        // choose a start Node from the possible List of Nodes
        // TODO: how to choose the start node?
        ArrayList<NodeImpl> startNodes = processDefinition.getStartNodes();
        currentNode = startNodes.get(startNumber);

    }

    // Just for testing purposes => make the start easy as possible without a
    // process definition
    public ProcessInstanceImpl(ArrayList<Node> nodes) {

        currentNode = (NodeImpl) nodes.get(0);

    }

    public ProcessInstanceImpl(NodeImpl startNodes) {

        currentNode = startNodes;
    }

    public NodeImpl getCurrentNode() {

        return currentNode;
    }

    public ArrayList<ProcessInstanceImpl> getChildInstances() {

        return childInstances;
    }

    public void setChildInstances(ArrayList<ProcessInstanceImpl> childInstances) {

        this.childInstances = childInstances;
    }

    public String getID() {

        return id;
    }

    public void setID(String s) {

        id = s;

    }

    public void setVariable(String name, Object value) {

        getInstanceVariables().put(name, value);
    }

    public Object getVariable(String name) {

        return getInstanceVariables().get(name);
    }

    public void setCurrentNode(NodeImpl node) {

        currentNode = node;
    }

    private Map<String, Object> getInstanceVariables() {

        if (instanceVariables == null) {
            instanceVariables = new HashMap<String, Object>();
        }
        return instanceVariables;
    }

    public List<ProcessInstance> executeStep() {

        this.currentNode.getActivity().execute(this);
        return this.currentNode.getRoutingBehaviour().execute(this);
        // return this.currentNode.navigate(this);
    }

}
