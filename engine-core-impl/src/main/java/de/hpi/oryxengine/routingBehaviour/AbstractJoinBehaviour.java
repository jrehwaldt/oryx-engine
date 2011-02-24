package de.hpi.oryxengine.routingBehaviour;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import de.hpi.oryxengine.processInstance.ProcessInstance;

public abstract class AbstractJoinBehaviour implements JoinBehaviour {

    // Do not override this
    Logger logger = Logger.getRootLogger();
    public List<ProcessInstance> join(ProcessInstance instance) {

        List<ProcessInstance> instances = new LinkedList<ProcessInstance>();
        if (joinable(instance)) {
            instances = performJoin(instance);
        }
        return instances;
    }

    protected abstract boolean joinable(ProcessInstance instance);

    protected abstract List<ProcessInstance> performJoin(ProcessInstance instance);
}
