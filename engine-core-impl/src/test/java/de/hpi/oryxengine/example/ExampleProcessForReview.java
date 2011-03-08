package de.hpi.oryxengine.example;

import java.util.UUID;

import de.hpi.oryxengine.activity.AbstractActivity;

import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.factory.AddNumbersAndStoreNodeFactory;
import de.hpi.oryxengine.factory.MailNodeFactory;
import de.hpi.oryxengine.factory.PrintingNodeFactory;
import de.hpi.oryxengine.factory.RoutingBehaviourTestFactory;
import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.plugin.navigator.NavigatorListenerLogger;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.process.token.TokenImpl;


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
        
        TokenImpl token = processTokenForReview();
        navigator.startArbitraryInstance(UUID.randomUUID(), token);
        
        Thread.sleep(SLEEP_TIME);
        
        navigator.stop();
    }
    
    /**
     * Creates the process token for the reviewProcess.
     * 
     * @return the process token impl
     */
    private static TokenImpl processTokenForReview() {

        /*
         * The process looks like this: start => calc5Plus5 => printResult => mailingTheResult => end
         */
        
        // Default to gerardo.navarro-suarez@student.hpi.uni-potsdam.de
        AbstractActivity end = new EndActivity();

        Node startNode = new RoutingBehaviourTestFactory().createWithAndSplitAndLogger();

        Node secondNode = new AddNumbersAndStoreNodeFactory("result", 5, 5).createWithLogger();

        Node thirdNode = new PrintingNodeFactory().createWithLogger();

        Node fourthNode = new MailNodeFactory().createWithLogger();

        Node endNode = new NodeImpl(end);

        // Setting the transitions
        startNode.transitionTo(secondNode);
        secondNode.transitionTo(thirdNode);
        thirdNode.transitionTo(fourthNode);
        fourthNode.transitionTo(endNode);

        TokenImpl sampleToken = new TokenImpl(startNode);
        return sampleToken;
    }

}
