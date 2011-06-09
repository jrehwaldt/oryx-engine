package org.jodaengine.process.instance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.ControlFlow;

/**
 * The Class ProcessInstanceContextImpl.
 */
public class ProcessInstanceContextImpl implements ProcessInstanceContext {

    private static final int MAGIC_HASH_CONSTANT_TWO = 31;

    private static final int MAGIC_HASH_CONSTANT_ONE = 7;

    @JsonIgnore
    private Map<Node, List<ControlFlow>> waitingControlFlows;

    private Map<String, Object> contextVariables;
    private Map<String, Object> nodeVariables;

    /**
     * Instantiates a new process instance context impl.
     */
    public ProcessInstanceContextImpl() {

        waitingControlFlows = new HashMap<Node, List<ControlFlow>>();
    }

    @Override
    public void setWaitingExecution(ControlFlow t) {

        List<ControlFlow> tmpList;
        if (waitingControlFlows.get(t.getDestination()) != null) {
            tmpList = waitingControlFlows.get(t.getDestination());

        } else {
            tmpList = new ArrayList<ControlFlow>();
            waitingControlFlows.put(t.getDestination(), tmpList);
        }
        tmpList.add(t);

    }

    @Override
    public List<ControlFlow> getWaitingExecutions(Node n) {

        return waitingControlFlows.get(n);
    }

    @Override
    public boolean allIncomingControlFlowsSignaled(Node n) {

        List<ControlFlow> signaledControlFlows = waitingControlFlows.get(n);

        // If there are no waiting {@link ControlFlow}s yet, the list might not be initialized yet
        if (signaledControlFlows == null) {
            return false;
        }
        List<ControlFlow> incomingControlFlows = n.getIncomingControlFlows();
        return signaledControlFlows.containsAll(incomingControlFlows);
    }

    @Override
    public void removeIncomingControlFlows(Node node) {

        List<ControlFlow> signaledControlFlows = waitingControlFlows.get(node);
        List<ControlFlow> incomingControlFlows = node.getIncomingControlFlows();
        removeSubset(signaledControlFlows, incomingControlFlows);

    }

    /**
     * Removes the subset.
     * 
     * @param superList
     *            the super list
     * @param subList
     *            the sub list
     */
    private void removeSubset(List<ControlFlow> superList, List<ControlFlow> subList) {

        for (ControlFlow t : subList) {
            superList.remove(t);
        }
    }

    @Override
    public Map<String, Object> getVariableMap() {

        return getInstanceVariables();
    }

    @Override
    public void setVariable(String name, Object value) {

        getInstanceVariables().put(name, value);

    }

    @Override
    public Object getVariable(String name) {

        return getInstanceVariables().get(name);
    }

    /**
     * Gets the instance variables.
     * 
     * @return the instance variables
     */
    @JsonProperty
    private Map<String, Object> getInstanceVariables() {

        if (contextVariables == null) {
            contextVariables = Collections.synchronizedMap(new HashMap<String, Object>());
        }
        return contextVariables;
    }

    @Override
    public boolean equals(@Nullable Object object) {

        if (object instanceof ProcessInstanceContextImpl) {
            ProcessInstanceContextImpl context = (ProcessInstanceContextImpl) object;

            if (getInstanceVariables().equals(context.getInstanceVariables())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {

        int hash = MAGIC_HASH_CONSTANT_ONE;
        int magicHashSummand = 0;

        Object o;
        for (String key : getInstanceVariables().keySet()) {
            o = getInstanceVariables().get(key);
            if (null == o) {
                magicHashSummand = 0;
            } else {
                magicHashSummand = o.hashCode();
            }
            hash = MAGIC_HASH_CONSTANT_TWO * hash + magicHashSummand;
        }

        return hash;
    }

    @Override
    public void setNodeVariable(Node node, String name, Object value) {

        String variableName = generateNodeVariableIdentifier(node, name);
        getInternalVariables().put(variableName, value);
    }

    @Override
    public Object getNodeVariable(Node node, String name) {

        String variableName = generateNodeVariableIdentifier(node, name);
        return getInternalVariables().get(variableName);
    }

    /**
     * Lazily initializes the map of internal variables. It is a synchronized map as concurrent access to the variable
     * map is likely to occur. We use only one map to handle not that much nested data structures.
     * 
     * @return the internal variables map
     */
    @JsonIgnore
    private Map<String, Object> getInternalVariables() {

        if (nodeVariables == null) {
            nodeVariables = Collections.synchronizedMap(new HashMap<String, Object>());
        }
        return nodeVariables;
    }
    
    private String generateNodeVariableIdentifier(Node node, String variableName) {
        return node.getID() + "-" + variableName;
    }

    @Override
    public void removeIncomingControlFlow(ControlFlow controlFlow, Node node) {

        List<ControlFlow> signaledControlFlows = waitingControlFlows.get(controlFlow.getDestination());
        // The map is referencing to every Destination all the incoming Transitions. But we need to check the start node, in order to delete it.
        for(ControlFlow t : signaledControlFlows) {
            if(t.getSource() == node) {
                List<ControlFlow> incomingControlFlows = new ArrayList<ControlFlow>();
                incomingControlFlows.add(controlFlow);
                removeSubset(signaledControlFlows, incomingControlFlows);
                break;
            }
        }

        
    }
}
