package de.hpi.oryxengine.processInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.hpi.oryxengine.processDefinition.AbstractProcessDefinitionImpl;
import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.NodeImpl;
import de.hpi.oryxengine.processstructure.Transition;

public class ProcessInstanceImpl implements ProcessInstance {

    private String id;
    private Node currentNode;
    private ProcessInstance parentInstance;
    private List<ProcessInstance> childInstances;
    private Map<String, Object> instanceVariables;

    public ProcessInstanceImpl(AbstractProcessDefinitionImpl processDefinition, Integer startNumber) {

        // choose a start Node from the possible List of Nodes
<<<<<<< HEAD:src/main/java/de/hpi/oryxengine/processInstanceImpl/ProcessInstanceImpl.java
        // TODO how to choose the start node?
        ArrayList<NodeImpl> startNodes = processDefinition.getStartNodes();
=======
        // TODO: how to choose the start node?
        ArrayList<Node> startNodes = processDefinition.getStartNodes();
>>>>>>> remotes/origin/modular-maven-setup:engine-core-impl/src/main/java/de/hpi/oryxengine/processInstance/ProcessInstanceImpl.java
        currentNode = startNodes.get(startNumber);

    }

    public ProcessInstanceImpl(Node startNode) {

        this(startNode, null);
    }

    public ProcessInstanceImpl(Node startNode, ProcessInstance parentInstance) {

        currentNode = startNode;
        this.parentInstance = parentInstance;
        this.childInstances = new ArrayList<ProcessInstance>();
    }

    public ProcessInstance getParentInstance() {

        return parentInstance;
    }

    public void setParentInstance(ProcessInstance instance) {

        this.parentInstance = instance;
    }

    public Node getCurrentNode() {

        return currentNode;
    }

    public void setCurrentNode(Node node) {

        currentNode = node;
    }

    public List<ProcessInstance> getChildInstances() {

        return childInstances;
    }

    public void setChildInstances(List<ProcessInstance> childInstances) {

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

    public List<ProcessInstance> takeAllTransitions() {

        List<ProcessInstance> instancesToNavigate = new LinkedList<ProcessInstance>();
        ArrayList<Transition> transitions = this.getCurrentNode().getTransitions();
        if (transitions.size() == 1) {
            Transition transition = transitions.get(0);
            Node destination = transition.getDestination();
            this.setCurrentNode(destination);
            instancesToNavigate.add(this);
        } else {
            for (Transition transition : transitions) {
                Node destination = transition.getDestination();
                ProcessInstance childInstance = createChildInstance(destination);
                instancesToNavigate.add(childInstance);
            }
        }
        return instancesToNavigate;
    }

    public List<ProcessInstance> takeSingleTransition(Transition t) {

        List<ProcessInstance> instancesToNavigate = new LinkedList<ProcessInstance>();
        this.currentNode = t.getDestination();
        instancesToNavigate.add(this);
        return instancesToNavigate;
    }

    public ProcessInstance createChildInstance(Node node) {

        ProcessInstance childInstance = new ProcessInstanceImpl(node);
        childInstance.setParentInstance(this);
        this.childInstances.add(childInstance);
        return childInstance;
    }

}
