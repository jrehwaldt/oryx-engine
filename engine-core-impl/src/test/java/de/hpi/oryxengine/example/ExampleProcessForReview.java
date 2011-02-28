package de.hpi.oryxengine.example;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.MailingVariable;
import de.hpi.oryxengine.activity.impl.PrintingVariableActivity;
import de.hpi.oryxengine.activity.impl.StartActivity;
import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.routing.behaviour.RoutingBehaviour;
import de.hpi.oryxengine.routing.behaviour.impl.TakeAllBehaviour;

/**
 * The Class that holds the example process that needs as review process for the engine.
 */
public final class ExampleProcessForReview {
    
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
        navigator.start();

        ProcessInstanceImpl instance = processInstanceForReview();
        navigator.startArbitraryInstance("1", instance);

        Thread.sleep(5000);

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

        Activity start = new StartActivity();
        Activity calc5Plus5 = new AddNumbersAndStoreActivity(5, 5, "result");
        Activity printResult = new PrintingVariableActivity("result");
        // Default to gerardo.navarro-suarez@student.hpi.uni-potsdam.de
        Activity mailingResult = new MailingVariable("result");
        Activity end = new EndActivity();

        RoutingBehaviour behaviour = new TakeAllBehaviour();

        NodeImpl startNode = new NodeImpl(start, behaviour);
        startNode.setId("1");

        NodeImpl secondNode = new NodeImpl(calc5Plus5, behaviour);
        secondNode.setId("2");

        NodeImpl thirdNode = new NodeImpl(printResult, behaviour);
        thirdNode.setId("3");

        NodeImpl fourthNode = new NodeImpl(mailingResult, behaviour);
        fourthNode.setId("4");

        NodeImpl endNode = new NodeImpl(end);
        endNode.setId("5");

        // Setting the transitions
        startNode.transitionTo(secondNode);
        secondNode.transitionTo(thirdNode);
        thirdNode.transitionTo(fourthNode);
        fourthNode.transitionTo(endNode);

        ProcessInstanceImpl sampleInstance = new ProcessInstanceImpl(startNode);
        return sampleInstance;
    }

}
