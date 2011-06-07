package org.jodaengine.process.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jodaengine.node.activity.Activity;
import org.jodaengine.node.incomingbehaviour.IncomingBehaviour;
import org.jodaengine.node.outgoingbehaviour.OutgoingBehaviour;
import org.jodaengine.process.structure.condition.HashMapCondition;


/**
 * The Class AbstractNode. Which is used for the graph representation of a Process
 */
public class NodeImpl implements Node {
    
    /**
     * The {@link Activity}. This is the behavior of the node e.g. what gets executed.
     */
    private Activity activityBehaviour;

    private OutgoingBehaviour outgoingBehaviour;
    private IncomingBehaviour incomingBehaviour;

    private List<ControlFlow> outgoingControlFlows, incomingControlFlows;

    private UUID id;
    private Map<String, Object> attributes;

    /**
     * Hidden constructor.
     */
    protected NodeImpl() {

    }

    /**
     * Instantiates a new abstract node.
     * 
     * @param blueprint
     *            the blueprint of the activity that is to instantiate when the node is reached by a token
     * @param incomingBehaviour
     *            the incoming behavior
     * @param outgoingBehaviour
     *            the outgoing behavior
     */
    public NodeImpl(Activity activityBehavior, IncomingBehaviour incomingBehaviour, OutgoingBehaviour outgoingBehaviour) {

        this.activityBehaviour = activityBehavior;
        this.incomingBehaviour = incomingBehaviour;
        this.outgoingBehaviour = outgoingBehaviour;
        this.outgoingControlFlows = new ArrayList<ControlFlow>();
        this.incomingControlFlows = new ArrayList<ControlFlow>();

        this.id = UUID.randomUUID();
    }

    @Override
    public OutgoingBehaviour getOutgoingBehaviour() {

        return outgoingBehaviour;
    }

    @Override
    public IncomingBehaviour getIncomingBehaviour() {

        return incomingBehaviour;
    }

    @Override
    public ControlFlow controlFlowTo(Node node) {

        Condition condition = new HashMapCondition();
        return createControlFlowWithCondition(node, condition);
    }

    @Override
    public ControlFlow controlFlowToWithCondition(Node node, Condition c) {

        return createControlFlowWithCondition(node, c);
    }

    /**
     * Creates the {@link ControlFlow} with condition.
     * 
     * @param node
     *            the destination
     * @param c
     *            the condition
     */
    private ControlFlow createControlFlowWithCondition(Node node, Condition c) {

        ControlFlow controlFlow = new ControlFlowImpl(this, node, c);
        this.outgoingControlFlows.add(controlFlow);
        List<ControlFlow> nextIncoming = node.getIncomingControlFlows();
        nextIncoming.add(controlFlow);

        return controlFlow;
    }

    @Override
    public UUID getID() {

        return this.id;
    }

    /**
     * Sets the {@link ControlFlow}s.
     * 
     * @param controlFlows
     *            the new {@link ControlFlow}s
     */
    public void setControlFlows(List<ControlFlow> controlFlows) {

        this.outgoingControlFlows = controlFlows;
    }

    @Override
    public List<ControlFlow> getOutgoingControlFlows() {

        return outgoingControlFlows;
    }

    @Override
    public List<ControlFlow> getIncomingControlFlows() {

        return incomingControlFlows;
    }

    @JsonProperty
    @Override
    public Map<String, Object> getAttributes() {

        if (attributes == null) {
            attributes = new HashMap<String, Object>();
        }
        return attributes;
    }

    @Override
    public Object getAttribute(String attributeKey) {

        return getAttributes().get(attributeKey);
    }

    @Override
    public void setAttribute(String attributeKey, Object attributeValue) {

        getAttributes().put(attributeKey, attributeValue);
    }

    @Override
    public Activity getActivityBehaviour() {

        return this.activityBehaviour;
    }
    
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
    
    @Override
    public boolean equals(Object object) {
        
        //
        // will never be equal to null
        //
        if (object == null) {
            return false;
        }
        
        //
        // or to a non-Node instance
        //
        if (object instanceof Node) {
            Node node = (Node) object;
            
            //
            // same id
            //
            if (!this.getID().equals(node.getID())) {
                return false;
            }
            
            return true;
        }
        
        return false;
    }
}
