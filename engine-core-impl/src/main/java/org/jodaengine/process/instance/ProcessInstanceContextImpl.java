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
import org.jodaengine.process.structure.Transition;


/**
 * The Class ProcessInstanceContextImpl.
 */
public class ProcessInstanceContextImpl implements ProcessInstanceContext {
    
    private static final int MAGIC_HASH_CONSTANT_TWO = 31;

    private static final int MAGIC_HASH_CONSTANT_ONE = 7;

    @JsonIgnore
    private Map<Node, List<Transition>> waitingTransitions;

    private Map<String, Object> contextVariables;
    
    private Map<String, Object> internalVariables;

    /**
     * Instantiates a new process instance context impl.
     */
    public ProcessInstanceContextImpl() {

        waitingTransitions = new HashMap<Node, List<Transition>>();
    }

    @Override
    public void setWaitingExecution(Transition t) {

        List<Transition> tmpList;
        if (waitingTransitions.get(t.getDestination()) != null) {
            tmpList = waitingTransitions.get(t.getDestination());

        } else {
            tmpList = new ArrayList<Transition>();
            waitingTransitions.put(t.getDestination(), tmpList);
        }
        tmpList.add(t);

    }

    @Override
    public List<Transition> getWaitingExecutions(Node n) {

        return waitingTransitions.get(n);
    }

    @Override
    public boolean allIncomingTransitionsSignaled(Node n) {

        List<Transition> signaledTransitions = waitingTransitions.get(n);

        // If there are no waiting transitions yet, the list might not be initialized yet
        if (signaledTransitions == null) {
            return false;
        }
        List<Transition> incomingTransitions = n.getIncomingTransitions();
        return signaledTransitions.containsAll(incomingTransitions);
    }

    @Override
    public void removeSignaledTransitions(Node node) {

        List<Transition> signaledTransitions = waitingTransitions.get(node);
        List<Transition> incomingTransitions = node.getIncomingTransitions();
        removeSubset(signaledTransitions, incomingTransitions);

    }

    /**
     * Removes the subset.
     * 
     * @param superList
     *            the super list
     * @param subList
     *            the sub list
     */
    private void removeSubset(List<Transition> superList, List<Transition> subList) {

        for (Transition t : subList) {
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
        for (String key: getInstanceVariables().keySet()) {
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
    
    /**
     * Gets the internal variables.
     *
     * @return the internal variables
     */
    @JsonIgnore
    private Map<String, Object> getInternalVariables() {
        
        if (internalVariables == null) {
            internalVariables = Collections.synchronizedMap(new HashMap<String, Object>());
        }
        return internalVariables;
    }

    @Override
    public Object getInternalVariable(String id) {

        return getInternalVariables().get(id);
    }

    @Override
    public void setInternalVariable(String variableId, Object variableValue) {

        getInternalVariables().put(variableId, variableValue);
        
    }

    @Override
    public void deleteInternalVariable(String id) {

        getInternalVariables().remove(id);
        
    }
}
