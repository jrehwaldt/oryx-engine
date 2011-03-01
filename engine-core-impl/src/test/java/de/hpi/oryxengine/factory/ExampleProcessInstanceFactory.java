package de.hpi.oryxengine.factory;

import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.structure.Node;

/**
 * A factory for creating ExampleProcessInstance objects.
 * These objects just have 2 add Number activities.
 */
public class ExampleProcessInstanceFactory {
    private Node node1;
    private Node node2;
    private ProcessInstance p;
    
    /**
     * Creates an Example process instance which uses just two AddNumbersAndStore Nodes.. a bit of profiling
     *
     * @return the process instance
     */
    public ProcessInstance create() {
        this.initializeNodes();
        this.initializeProcessInstance();
        return this.p;
    }
    
    /**
     * Initializes the nodes.
     */
    public void initializeNodes() {
        AddNumbersAndStoreNodeFactory factory = new AddNumbersAndStoreNodeFactory();
        node1 = factory.create();
        node2 = factory.create();
        node1.transitionTo(node2);
    }
    
    /**
     * Initializes the process instance.
     */
    public void initializeProcessInstance() {
        SimpleProcessInstanceFactory factory = new SimpleProcessInstanceFactory();
        p = factory.create(node1);        
    }

}
