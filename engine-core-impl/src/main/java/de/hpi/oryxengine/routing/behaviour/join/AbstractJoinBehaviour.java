package de.hpi.oryxengine.routing.behaviour.join;

import java.util.LinkedList;
import java.util.List;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.routingBehaviour.joinBehaviour.JoinBehaviour;

/**
 * The Class AbstractJoinBehaviour.
 */
public abstract class AbstractJoinBehaviour implements JoinBehaviour {

    /**
     * @see de.hpi.oryxengine.routingBehaviour.joinBehaviour.JoinBehaviour
     *      #join(de.hpi.oryxengine.processInstance.ProcessInstance) Do not override this, as it is a template method.
     *      Only join, if a join is possible.
     * @param instance instance to perform the join on
     * @return the list of joined instances
     */
    public List<ProcessInstance> join(ProcessInstance instance) {

        List<ProcessInstance> instances = new LinkedList<ProcessInstance>();
        if (joinable(instance)) {
            instances = performJoin(instance);
        }
        return instances;
    }

    /**
     * Joinable.
     * 
     * @param instance
     *            the instance that the join is triggered for.
     * @return true, if a join can be performed, e.g. for a BPMN AND-Join, all sibling-instances have to reached the
     *         join node as well.
     */
    protected abstract boolean joinable(ProcessInstance instance);

    /**
     * Perform join.
     * 
     * @param instance
     *            the instance that the join is triggered for.
     * @return the result of the join.
     */
    protected abstract List<ProcessInstance> performJoin(ProcessInstance instance);
}
