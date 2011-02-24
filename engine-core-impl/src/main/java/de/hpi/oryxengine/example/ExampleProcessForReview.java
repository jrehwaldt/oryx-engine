package de.hpi.oryxengine.example;

import java.util.ArrayList;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.MailingVariable;
import de.hpi.oryxengine.activity.impl.PrintingVariableActivity;
import de.hpi.oryxengine.activity.impl.StartActivity;
import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.processInstance.ProcessInstanceImpl;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.NodeImpl;
import de.hpi.oryxengine.routingBehaviour.BPMNTakeAllBehaviour;
import de.hpi.oryxengine.routingBehaviour.RoutingBehaviour;

public class ExampleProcessForReview {

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args)
    throws InterruptedException {

        NavigatorImpl navigator = new NavigatorImpl();
        navigator.start();

        ProcessInstanceImpl instance = processInstanceForReview();
        navigator.startArbitraryInstance("1", instance);

        Thread.sleep(5000);

        navigator.stop();
    }

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

        RoutingBehaviour behaviour = new BPMNTakeAllBehaviour();

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
