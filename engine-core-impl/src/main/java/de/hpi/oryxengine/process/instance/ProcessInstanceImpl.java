package de.hpi.oryxengine.process.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hpi.oryxengine.process.definition.AbstractProcessDefinitionImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;

/**
 * The implementation of a process instance.
 */
public class ProcessInstanceImpl
implements ProcessInstance {

    /** The id. */
    private UUID id;

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
     * @param processDefinition the process definition
     * @param startNumber the start number
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

    /**
     * Gets the parent processinstance. If we split, we create new process instances, that have a parent instance, the
     * instance where they originated
     * 
     * @return the parent instance
     * @see de.hpi.oryxengine.process.instance.ProcessInstance#getParentInstance()
     */
    public ProcessInstance getParentInstance() {

        return parentInstance;
    }

    /**
     * Sets the parentInstance of this instance. The processinstance this instance got forked from that is.
     * 
     * @param instance
     *            the new parent instance
     * @see de.hpi.oryxengine.process.instance.ProcessInstance
     *      #setParentInstance(de.hpi.oryxengine.process.instance.ProcessInstance )
     */
    public void setParentInstance(ProcessInstance instance) {

        this.parentInstance = instance;
    }

    /**
     * Gets the current node. So the position where the execution of the Processinstance is at.
     * 
     * @return the current node
     * @see de.hpi.oryxengine.process.instance.ProcessInstance#getCurrentNode()
     */
    public Node getCurrentNode() {

        return currentNode;
    }

    /**
     * Sets the current node. So the node where the execution of the process instance currently is at.
     * 
     * @param node
     *            the new current node
     * @see de.hpi.oryxengine.process.instance.ProcessInstance#setCurrentNode(de.hpi.oryxengine.process.structure.Node)
     */
    public void setCurrentNode(Node node) {

        currentNode = node;
    }

    /**
     * Gets the child instances. Childisntances are instances that got forked from here.
     * 
     * @return the child instances
     * @see de.hpi.oryxengine.process.instance.ProcessInstance#getChildInstances()
     */
    public List<ProcessInstance> getChildInstances() {

        return childInstances;
    }

    /**
     * Sets the child instances. Childisntances are instances that got forked from here.
     * 
     * @param childInstances
     *            the new child instances
     * @see de.hpi.oryxengine.process.instance.ProcessInstance#setChildInstances(java.util.List)
     */
    public void setChildInstances(List<ProcessInstance> childInstances) {

        this.childInstances = childInstances;
    }

    /**
     * Gets the ID of the processinstance.
     * 
     * @return the iD
     * @see de.hpi.oryxengine.process.instance.ProcessInstance#getID()
     */
    public UUID getID() {
        return id;
    }

    /**
     * Sets the variable.
     * 
     * @param name
     *            name of the variable
     * @param value
     *            the value to be set at the variable
     * @see de.hpi.oryxengine.process.instance.ProcessInstance#setVariable(java.lang.String, java.lang.Object)
     */
    public void setVariable(String name, Object value) {

        getInstanceVariables().put(name, value);
    }

    /**
     * Gets the variable.
     * 
     * @param name
     *            of the variable
     * @return the variable
     * @see de.hpi.oryxengine.process.instance.ProcessInstance#getVariable(java.lang.String)
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
     * Execute step.
     * 
     * @return list of process instances
     * @see de.hpi.oryxengine.process.instance.ProcessInstance#executeStep()
     */
    public List<ProcessInstance> executeStep() {

        return this.currentNode.execute(this);
    }


    public List<ProcessInstance> navigateTo(List<Node> nodeList)
    throws IllegalNavigationException {

        validateNodeList(nodeList);
        List<ProcessInstance> instancesToNavigate = new ArrayList<ProcessInstance>();
        if (nodeList.size() == 1) {
            Node node = nodeList.get(0);
            this.setCurrentNode(node);
            instancesToNavigate.add(this);
        } else {
            for (Node node : nodeList) {
                ProcessInstance childInstance = createChildInstance(node);
                instancesToNavigate.add(childInstance);
            }
        }
        return instancesToNavigate;

    }

    /**
     * Validate node list.
     * 
     * @param nodeList
     *            the node list
     * @throws IllegalNavigationException
     *             the illegal navigation exception
     */
    private void validateNodeList(List<Node> nodeList)
    throws IllegalNavigationException {

        ArrayList<Node> destinations = new ArrayList<Node>();
        for (Transition transition : currentNode.getTransitions()) {
            destinations.add(transition.getDestination());
        }
        if (!destinations.containsAll(nodeList)) {
            throw new IllegalNavigationException();
        }
    }

    /**
     * Take single transition.
     * 
     * @param t
     *            the transition to take
     * @return list of process instances
     * @see de.hpi.oryxengine.process.instance.ProcessInstance
     *      #takeSingleTransition(de.hpi.oryxengine.process.structure.Transition)
     */
    public List<ProcessInstance> takeSingleTransition(Transition t) {

        List<ProcessInstance> instancesToNavigate = new LinkedList<ProcessInstance>();
        this.currentNode = t.getDestination();
        instancesToNavigate.add(this);
        return instancesToNavigate;
    }

    /**
     * Creates the child instance.
     * 
     * @param node
     *            the node to add a child at
     * @return the child instance
     * @see de.hpi.oryxengine.process.instance.ProcessInstance
     *      #createChildInstance(de.hpi.oryxengine.process.structure.Node)
     */
    public ProcessInstance createChildInstance(Node node) {

        ProcessInstance childInstance = new ProcessInstanceImpl(node);
        childInstance.setParentInstance(this);
        this.childInstances.add(childInstance);
        return childInstance;
    }

}
