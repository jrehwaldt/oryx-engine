package de.hpi.oryxengine.example;

import org.apache.log4j.Logger;

import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;
import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.routing.behaviour.impl.TakeAllBehaviour;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;
/**
 * The Class SimpleExampleProcess. It really is just a simple example process.
 */
public class SimpleExampleProcess {

    /**
     * The Constant INSTANCE_COUNT. Which determines the number of instances which will be run when the main is
     * executed.
     */
    private static final int INSTANCE_COUNT = 1000000;

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
            navigator.startArbitraryInstance("1", instance);
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
        RoutingBehaviour behaviour = new TakeAllBehaviour();
        NodeImpl startNode = new NodeImpl(activity, behaviour);
        NodeImpl secondNode = new NodeImpl(activity2);
        startNode.setId("1");
        secondNode.setId("2");
        startNode.transitionTo(secondNode);

        ProcessInstanceImpl sampleInstance = new ProcessInstanceImpl(startNode);
        return sampleInstance;
    }

}
