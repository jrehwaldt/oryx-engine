package de.hpi.oryxengine.example;

import de.hpi.oryxengine.factory.node.HumanTaskNodeFactory;
import de.hpi.oryxengine.factory.node.PrintingNodeFactory;
import de.hpi.oryxengine.factory.node.RoutingBehaviourTestFactory;
import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.plugin.navigator.NavigatorListenerLogger;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.TokenImpl;


/**
 * The Class that holds the example process that needs as review process for the engine.
 */
public final class SimpleHumanTaskProcess {
    
    /** The Constant SLEEP_TIME. */
    private static final int SLEEP_TIME = 5000;

    /** Hidden constructor. */
    private SimpleHumanTaskProcess() {
        
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
        navigator.startArbitraryInstance(token);
        
        Thread.sleep(SLEEP_TIME);
        
        navigator.stop();
    }
    
    /**
     * Creates the process token for the reviewProcess.
     * 
     * @return the process token impl
     */
    private static TokenImpl processTokenForReview() {


        Node startNode = new RoutingBehaviourTestFactory().createWithAndSplitAndLogger();
        
        Node secondNode = new HumanTaskNodeFactory().createWithLogger();

        Node thirdNode = new PrintingNodeFactory().createWithLogger();
        
        // TODO @Gerardo: hier gilt dasselbe wie im ExampleProcessForReview.
//        Node endNode = new NodeImpl(BpmnEndActivity.class);

        // Setting the transitions
        startNode.transitionTo(secondNode);
        secondNode.transitionTo(thirdNode);
//        thirdNode.transitionTo(endNode);

        TokenImpl sampleToken = new TokenImpl(startNode);
        return sampleToken;
    }

}
