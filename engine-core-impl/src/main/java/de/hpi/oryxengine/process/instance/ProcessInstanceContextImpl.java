package de.hpi.oryxengine.process.instance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;

/**
 * The Class ProcessInstanceContextImpl.
 */
public class ProcessInstanceContextImpl implements ProcessInstanceContext {

    /** The waiting transitions. */
    private Map<Node, List<Transition>> waitingTransitions;

    /** The token variables. */
    private Map<String, Object> contextVariables;

    /**
     * Instantiates a new process instance context impl.
     */
    public ProcessInstanceContextImpl() {

        waitingTransitions = new HashMap<Node, List<Transition>>();
    }

    /**
     * Sets the waiting execution.
     * 
     * @param t
     *            the new waiting execution {@inheritDoc}
     */
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

    /**
     * Gets the waiting executions.
     *
     * @param n the n
     * @return the waiting executions
     * {@inheritDoc}
     */
    @Override
    public List<Transition> getWaitingExecutions(Node n) {

        return waitingTransitions.get(n);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeIncomingTransitions(Node n) {

        List<Transition> signaledTransitions = waitingTransitions.get(n);
        List<Transition> incomingTransitions = n.getIncomingTransitions();
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

    /**
     * Sets the variable.
     * 
     * @param name
     *            the name
     * @param value
     *            the value {@inheritDoc}
     */
    @Override
    public void setVariable(String name, Object value) {

        getInstanceVariables().put(name, value);
    }

    /**
     * Gets the variable.
     * 
     * @param name
     *            of the variable
     * @return the variable
     * @see de.hpi.oryxengine.process.token.Token#getVariable(java.lang.String)
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

        if (contextVariables == null) {
            contextVariables = Collections.synchronizedMap(new HashMap<String, Object>());
        }
        return contextVariables;
    }

}
