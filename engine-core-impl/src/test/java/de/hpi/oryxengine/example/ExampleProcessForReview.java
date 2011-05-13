package de.hpi.oryxengine.example;


import de.hpi.oryxengine.factory.node.AddNumbersAndStoreNodeFactory;
import de.hpi.oryxengine.factory.node.MailNodeFactory;
import de.hpi.oryxengine.factory.node.PrintingNodeFactory;
import de.hpi.oryxengine.factory.node.RoutingBehaviourTestFactory;
import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.node.activity.bpmn.BpmnEndActivity;
import de.hpi.oryxengine.plugin.navigator.NavigatorListenerLogger;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.NodeImpl;
import de.hpi.oryxengine.process.token.TokenImpl;

/**
 * The Class that holds the example process that needs as review process for the engine.
 */
public final class ExampleProcessForReview {
    
    /** The Constant SLEEP_TIME. */
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

        /*
         * The process looks like this: start => calc5Plus5 => printResult => mailingTheResult => end
         */
        
        // Default to gerardo.navarro-suarez@student.hpi.uni-potsdam.de
        Node startNode = new RoutingBehaviourTestFactory().createWithAndSplitAndLogger();

        Node secondNode = new AddNumbersAndStoreNodeFactory("result", 5, 5).createWithLogger();

        Node thirdNode = new PrintingNodeFactory().createWithLogger();

        Node fourthNode = new MailNodeFactory().createWithLogger();

        // TODO @Gerardo: Diese Klasse entweder zur korrekten Verwendung der JodaEngine umschreiben (Builder, deployen, etc.) oder löschen
        //die endNode muss dann wieder einkommentiert bzw. korrekt implementiert werden
//        Node endNode = new NodeImpl(BpmnEndActivity.class);

        // Setting the transitions
        startNode.transitionTo(secondNode);
        secondNode.transitionTo(thirdNode);
        thirdNode.transitionTo(fourthNode);
//        fourthNode.transitionTo(endNode);

        TokenImpl sampleToken = new TokenImpl(startNode);
        return sampleToken;
    }

}
