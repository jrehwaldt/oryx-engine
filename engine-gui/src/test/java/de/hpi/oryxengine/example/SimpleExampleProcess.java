package de.hpi.oryxengine.example;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;
import de.hpi.oryxengine.monitor.Monitor;
import de.hpi.oryxengine.monitor.MonitorGUI;
import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class SimpleExampleProcess. It really is just a simple example process.
 */
public final class SimpleExampleProcess {

    /** Hidden constructor. */
    private SimpleExampleProcess() {

    }

    /**
     * The Constant INSTANCE_COUNT. Which determines the number of instances which will be run when the main is
     * executed.
     */
    private static final int INSTANCE_COUNT = 1000000;

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleExampleProcess.class);

    /**
     * The main method. It starts a a specified number of instances.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {

        NavigatorImpl navigator = new NavigatorImpl();
        MonitorGUI monitorGUI = MonitorGUI.start(INSTANCE_COUNT);
        Monitor monitor = new Monitor(monitorGUI);
        navigator.getScheduler().registerPlugin(monitor);
        navigator.start();

        // let's generate some load :)
        LOGGER.info("Engine started");
        for (int i = 0; i < INSTANCE_COUNT; i++) {
            TokenImpl instance = sampleProcessInstance(i);
            if (i == 234000 || i == 100000 || i == 500000 || i == 800000) {
                monitor.markSingleInstance(instance);
            }
            navigator.startArbitraryInstance(instance);

            if (i % INSTANCE_COUNT == 0) {
                LOGGER.debug("Started {} Instances", i);
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
    private static TokenImpl sampleProcessInstance(int counter) {

        AutomatedDummyActivity activity = new AutomatedDummyActivity("I suck " + counter);
        AutomatedDummyActivity activity2 = new AutomatedDummyActivity("I suck of course " + counter);
        SimpleJoinBehaviour incoming = new SimpleJoinBehaviour();
        TakeAllSplitBehaviour outgoing = new TakeAllSplitBehaviour();
        NodeImpl startNode = new NodeImpl(activity, incoming, outgoing);
        NodeImpl secondNode = new NodeImpl(activity2);;
        startNode.transitionTo(secondNode);

        TokenImpl sampleInstance = new TokenImpl(startNode);
        return sampleInstance;
    }

}
