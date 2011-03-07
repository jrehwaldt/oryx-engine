package de.hpi.oryxengine.example;

import java.util.UUID;

import de.hpi.oryxengine.activity.AbstractActivity;
import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.MailingVariable;
import de.hpi.oryxengine.activity.impl.PrintingVariableActivity;
import de.hpi.oryxengine.activity.impl.StartActivity;
import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.plugin.activity.AbstractActivityLifecyclePlugin;
import de.hpi.oryxengine.plugin.activity.ActivityLifecycleLogger;
import de.hpi.oryxengine.plugin.navigator.NavigatorListenerLogger;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.routing.behaviour.RoutingBehaviour;
import de.hpi.oryxengine.routing.behaviour.impl.TakeAllBehaviour;

/**
 * The Class that holds the example process that needs as review process for the engine.
 */
public final class ExampleProcessForReview {
    
    private static final int SLEEP_TIME = 5000;

    /** Hidden constructor. */
    private ExampleProcessForReview() {
        
    }

    /**
     * The main method of our example process for review.
     * 
     * @param args
     *            the arguments
     * @throws InterruptedException
     *             the exception if an interrupt occurs
     */
    public static void main(String[] args)
    throws InterruptedException {
        
        // the main
        NavigatorImpl navigator = new NavigatorImpl();
        navigator.registerPlugin(NavigatorListenerLogger.getInstance());
        navigator.start();
        
        ProcessInstanceImpl instance = processInstanceForReview();
        navigator.startArbitraryInstance(UUID.randomUUID(), instance);
        
        Thread.sleep(SLEEP_TIME);
        
        navigator.stop();
    }
    
    /**
     * Creates the processinstance for the reviewProcess.
     * 
     * @return the process instance impl
     */
    private static ProcessInstanceImpl processInstanceForReview() {

        /*
         * The process looks like this: start => calc5Plus5 => printResult => mailingTheResult => end
         */
        AbstractActivityLifecyclePlugin lifecycleLogger = ActivityLifecycleLogger.getInstance();
        
        AbstractActivity start = new StartActivity();
        AbstractActivity calc5Plus5 = new AddNumbersAndStoreActivity("result", 5, 5);
        PrintingVariableActivity printResult = new PrintingVariableActivity("result");
        // Default to gerardo.navarro-suarez@student.hpi.uni-potsdam.de
        AbstractActivity mailResult = new MailingVariable("result");
        AbstractActivity end = new EndActivity();
        calc5Plus5.registerPlugin(lifecycleLogger);
        printResult.registerPlugin(lifecycleLogger);
        mailResult.registerPlugin(lifecycleLogger);
        end.registerPlugin(lifecycleLogger);

        RoutingBehaviour behaviour = new TakeAllBehaviour();

        NodeImpl startNode = new NodeImpl(start, behaviour);
        NodeImpl secondNode = new NodeImpl(calc5Plus5, behaviour);
        NodeImpl thirdNode = new NodeImpl(printResult, behaviour);
        NodeImpl fourthNode = new NodeImpl(mailResult, behaviour);
        NodeImpl endNode = new NodeImpl(end);

        // Setting the transitions
        startNode.transitionTo(secondNode);
        secondNode.transitionTo(thirdNode);
        thirdNode.transitionTo(fourthNode);
        fourthNode.transitionTo(endNode);

        ProcessInstanceImpl sampleInstance = new ProcessInstanceImpl(startNode);
        return sampleInstance;
    }

}
