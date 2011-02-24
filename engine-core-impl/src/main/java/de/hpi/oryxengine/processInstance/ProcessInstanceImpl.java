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

// TODO: Auto-generated Javadoc
/**
 * The Class ProcessInstanceImpl.
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
     * de.hpi.oryxengine.processInstance.ProcessInstance#setParentInstance(de.hpi.oryxengine.processInstance.ProcessInstance
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

    /*
     * (non-Javadoc)
     * 
     * @see de.hpi.oryxengine.processInstance.ProcessInstance#setVariable(java.lang.String, java.lang.Object)
     */
    public void setVariable(String name, Object value) {

        getInstanceVariables().put(name, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hpi.oryxengine.processInstance.ProcessInstance#getVariable(java.lang.String)
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

    /*
     * (non-Javadoc)
     * 
     * @see de.hpi.oryxengine.processInstance.ProcessInstance#executeStep()
     */
    public List<ProcessInstance> executeStep() {

        this.currentNode.getActivity().execute(this);
        return this.currentNode.getRoutingBehaviour().execute(this);
        // return this.currentNode.navigate(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.hpi.oryxengine.processInstance.ProcessInstance#takeAllTransitions()
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.hpi.oryxengine.processInstance.ProcessInstance#takeSingleTransition(de.hpi.oryxengine.processstructure.Transition
     * )
     */
    public List<ProcessInstance> takeSingleTransition(Transition t) {

        List<ProcessInstance> instancesToNavigate = new LinkedList<ProcessInstance>();
        this.currentNode = t.getDestination();
        instancesToNavigate.add(this);
        return instancesToNavigate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.hpi.oryxengine.processInstance.ProcessInstance#createChildInstance(de.hpi.oryxengine.processstructure.Node)
     */
    public ProcessInstance createChildInstance(Node node) {

        ProcessInstance childInstance = new ProcessInstanceImpl(node);
        childInstance.setParentInstance(this);
        this.childInstances.add(childInstance);
        return childInstance;
    }

}
