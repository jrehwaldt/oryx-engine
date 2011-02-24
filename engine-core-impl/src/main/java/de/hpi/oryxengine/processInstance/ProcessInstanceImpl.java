package de.hpi.oryxengine.processInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.hpi.oryxengine.processDefinition.AbstractProcessDefinitionImpl;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.Transition;

/**
 * The implementation of a process instance.
 */
public class ProcessInstanceImpl implements ProcessInstance {

    /** The id. */
    private String id;

    /** The current node. */
    private Node currentNode;

    /** The parent instance. */
    private ProcessInstance parentInstance;

    /** The child instances. */
    private List<ProcessInstance> childInstances;

    /** The instance variables. */
    private Map<String, Object> instanceVariables;

    /**
     * Instantiates a new process instance impl.
     * 
     * @param processDefinition
     *            the process definition
     * @param startNumber
     *            the start number
     */
    public ProcessInstanceImpl(AbstractProcessDefinitionImpl processDefinition, Integer startNumber) {

        // choose a start Node from the possible List of Nodes
        // TODO: how to choose the start node?
        ArrayList<Node> startNodes = processDefinition.getStartNodes();
        currentNode = startNodes.get(startNumber);

    }

    /**
     * Instantiates a new process instance impl.
     * 
     * @param startNode
     *            the start node
     */
    public ProcessInstanceImpl(Node startNode) {

        this(startNode, null);
    }

    /**
     * Instantiates a new process instance impl.
     * 
     * @param startNode
     *            the start node
     * @param parentInstance
     *            the parent instance
     */
    public ProcessInstanceImpl(Node startNode, ProcessInstance parentInstance) {

        currentNode = startNode;
        this.parentInstance = parentInstance;
        this.childInstances = new ArrayList<ProcessInstance>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hpi.oryxengine.processInstance.ProcessInstance#getParentInstance()
     */
    public ProcessInstance getParentInstance() {

        return parentInstance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.hpi.oryxengine.processInstance.ProcessInstance
     *      #setParentInstance(de.hpi.oryxengine.processInstance.ProcessInstance
     * )
     */
    public void setParentInstance(ProcessInstance instance) {

        this.parentInstance = instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hpi.oryxengine.processInstance.ProcessInstance#getCurrentNode()
     */
    public Node getCurrentNode() {

        return currentNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hpi.oryxengine.processInstance.ProcessInstance#setCurrentNode(de.hpi.oryxengine.processstructure.Node)
     */
    public void setCurrentNode(Node node) {

        currentNode = node;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hpi.oryxengine.processInstance.ProcessInstance#getChildInstances()
     */
    public List<ProcessInstance> getChildInstances() {

        return childInstances;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hpi.oryxengine.processInstance.ProcessInstance#setChildInstances(java.util.List)
     */
    public void setChildInstances(List<ProcessInstance> childInstances) {

        this.childInstances = childInstances;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hpi.oryxengine.processInstance.ProcessInstance#getID()
     */
    public String getID() {

        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hpi.oryxengine.processInstance.ProcessInstance#setID(java.lang.String)
     */
    public void setID(String s) {

        id = s;

    }

    /**
     * @see de.hpi.oryxengine.processInstance.ProcessInstance#setVariable(java.lang.String, java.lang.Object)
     * @param name name of the variable
     * @param value the value to be set at the variable
     */
    public void setVariable(String name, Object value) {

        getInstanceVariables().put(name, value);
    }

    /**
     * @see de.hpi.oryxengine.processInstance.ProcessInstance#getVariable(java.lang.String)
     * @param name of the variable
     * @return the variable
     */
    public Object getVariable(String name) {

        return getInstanceVariables().get(name);
    }

    /**
     * Gets the instance variables.
     * 
     * @return the instance variables
     */
    private Map<String, Object> getInstanceVariables() {

        if (instanceVariables == null) {
            instanceVariables = new HashMap<String, Object>();
        }
        return instanceVariables;
    }

    /**
     * @see de.hpi.oryxengine.processInstance.ProcessInstance#executeStep()
     * @return list of process instances
     */
    public List<ProcessInstance> executeStep() {

        // TODO Hey this is uncool if we got uncontrolled BPMN control flow. We would execute the node multiple times
        this.currentNode.getActivity().execute(this);
        return this.currentNode.getRoutingBehaviour().execute(this);
        // return this.currentNode.navigate(this);
    }

    /**
     * @see de.hpi.oryxengine.processInstance.ProcessInstance
     *      #takeAllTransitions()
     * @return list of process instances 
     */
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

    /**
     * @see de.hpi.oryxengine.processInstance.ProcessInstance
     *      #takeSingleTransition(de.hpi.oryxengine.processstructure.Transition)
     * @param t the transition to take
     * @return list of process instances
     */
    public List<ProcessInstance> takeSingleTransition(Transition t) {

        List<ProcessInstance> instancesToNavigate = new LinkedList<ProcessInstance>();
        this.currentNode = t.getDestination();
        instancesToNavigate.add(this);
        return instancesToNavigate;
    }

    /**
     *@see de.hpi.oryxengine.processInstance.ProcessInstance
     *      #createChildInstance(de.hpi.oryxengine.processstructure.Node)
     *@param node the node to add a child at
     *@return the child instance
     */
    public ProcessInstance createChildInstance(Node node) {

        ProcessInstance childInstance = new ProcessInstanceImpl(node);
        childInstance.setParentInstance(this);
        this.childInstances.add(childInstance);
        return childInstance;
    }

}
