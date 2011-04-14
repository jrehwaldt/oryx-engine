package de.hpi.oryxengine.example;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.activity.impl.AutomatedDummyActivity;
import de.hpi.oryxengine.monitor.Monitor;
import de.hpi.oryxengine.monitor.MonitorGUI;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.ActivityBlueprint;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.process.token.TokenImpl;

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
    
    private static final int STOPPING_MARK_1 = 234000;
    private static final int STOPPING_MARK_2 = 100000;
    private static final int STOPPING_MARK_3 = 500000;
    private static final int STOPPING_MARK_4 = 800000;

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
            TokenImpl instance = sampleProcessInstance(i, navigator);
            if (i == STOPPING_MARK_1 || i == STOPPING_MARK_2 || i == STOPPING_MARK_3 || i == STOPPING_MARK_4) {
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
     * @param counter the counter
     * @param navigator the navigator
     * @return the process instance impl
     */
    private static TokenImpl sampleProcessInstance(int counter, Navigator navigator) {

//        AutomatedDummyActivity activity = new AutomatedDummyActivity("I suck " + counter);
//        AutomatedDummyActivity activity2 = new AutomatedDummyActivity("I suck of course " + counter);
        // TODO parameters
        Class<?>[] constructorSig = {String.class};
        Object[] params = {"I suck " + counter};
        ActivityBlueprint blueprint = new ActivityBlueprintImpl(AutomatedDummyActivity.class, constructorSig,
            params);
        
        NodeImpl startNode = new NodeImpl(blueprint);
        
        params = new Object[] {"I suck of course " + counter};
        blueprint = new ActivityBlueprintImpl(AutomatedDummyActivity.class, constructorSig,
            params);
        NodeImpl secondNode = new NodeImpl(blueprint);
        startNode.transitionTo(secondNode);

        TokenImpl sampleInstance = new TokenImpl(startNode, new ProcessInstanceImpl(null), navigator);
        return sampleInstance;
    }

}
