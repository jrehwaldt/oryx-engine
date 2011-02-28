package de.hpi.oryxengine.example;

import java.util.UUID;

import org.apache.log4j.Logger;

import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;
import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.routing.behaviour.RoutingBehaviour;
import de.hpi.oryxengine.routing.behaviour.impl.TakeAllBehaviour;
import de.hpi.oryxengine.routing.behaviour.impl.XORBehaviour;
/**
 * The Class SimpleExampleProcess. It really is just a simple example process.
 */
public final class SimpleXORExampleProcess {
    
    /** Hidden constructor. */
    private SimpleXORExampleProcess() {
        
    }
    
    /**
     * The Constant INSTANCE_COUNT. Which determines the number of instances which will be run when the main is
     * executed.
     */
    private static final int INSTANCE_COUNT = 1;

    /** The logger. */
    private static Logger logger = Logger.getRootLogger();

    /**
     * The main method. It starts a a specified number of instances.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {

        NavigatorImpl navigator = new NavigatorImpl();
        navigator.start();

        // let's generate some load :)
        logger.info("Engine started");
        for (int i = 0; i < INSTANCE_COUNT; i++) {
            ProcessInstanceImpl instance = sampleProcessInstance(i);
            navigator.startArbitraryInstance(UUID.randomUUID(), instance);
            if (i % INSTANCE_COUNT == 0) {
                logger.debug("Started " + i + " Instances");
            }
        }
    }

    /**
     * Sample process instance.
     * 
     * @param counter
     *            the counter
     * @return the process instance impl
     */
    private static ProcessInstanceImpl sampleProcessInstance(int counter) {

        AutomatedDummyActivity activity = new AutomatedDummyActivity("I suck " + counter);
        AutomatedDummyActivity activity2 = new AutomatedDummyActivity("I suck of course " + counter);
        AutomatedDummyActivity activity3 = new AutomatedDummyActivity("I suck of course 3");
        RoutingBehaviour behaviour = new XORBehaviour();
        NodeImpl startNode = new NodeImpl(activity, behaviour);
        NodeImpl secondNode = new NodeImpl(activity3);
        NodeImpl thirdNode = new NodeImpl(activity2);
        startNode.setId("1");
        secondNode.setId("2");
        secondNode.setId("3");
        startNode.transitionTo(secondNode);
        startNode.transitionTo(thirdNode);

        ProcessInstanceImpl sampleInstance = new ProcessInstanceImpl(startNode);
        return sampleInstance;
    }

}
