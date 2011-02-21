package de.hpi.oryxengine.example;

import java.util.ArrayList;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.MailingVariable;
import de.hpi.oryxengine.activity.impl.PrintingVariableActivity;
import de.hpi.oryxengine.activity.impl.StartActivity;
import de.hpi.oryxengine.navigator.impl.NavigatorImpl;
import de.hpi.oryxengine.processInstanceImpl.ProcessInstanceImpl;
import de.hpi.oryxengine.processstructure.Node;
import de.hpi.oryxengine.processstructure.impl.NodeImpl;

public class ExampleProcessForReview {

  /**
   * @param args
   */
  public static void main(String[] args) {
    NavigatorImpl navigator = new NavigatorImpl();
    navigator.start();

    ProcessInstanceImpl instance = processInstanceForReview();
    navigator.startArbitraryInstance("1", instance);
  }

  private static ProcessInstanceImpl processInstanceForReview() {

    /*
     * The process looks like this: start => calc5Plus5 => printResult =>
     * mailingTheResult => end
     */

    Activity start = new StartActivity();
    Activity calc5Plus5 = new AddNumbersAndStoreActivity(5, 5, "result");
    Activity printResult = new PrintingVariableActivity("result");
    // Default to gerardo.navarro-suarez@student.hpi.uni-potsdam.de
    Activity mailingResult = new MailingVariable("result");
    Activity end = new EndActivity();

    NodeImpl startNode = new NodeImpl(start);
    startNode.setId("1");

    NodeImpl secondNode = new NodeImpl(calc5Plus5);
    secondNode.setId("2");

    NodeImpl thirdNode = new NodeImpl(printResult);
    thirdNode.setId("3");

    NodeImpl fourthNode = new NodeImpl(mailingResult);
    fourthNode.setId("4");

    NodeImpl endNode = new NodeImpl(end);
    endNode.setId("5");

    // Setting the transitions
    startNode.transitionTo(secondNode);
    secondNode.transitionTo(thirdNode);
    thirdNode.transitionTo(fourthNode);
    fourthNode.transitionTo(endNode);

    ArrayList<Node> startNodes = new ArrayList<Node>();
    startNodes.add(startNode);

    ProcessInstanceImpl sampleInstance = new ProcessInstanceImpl(startNodes);
    return sampleInstance;
  }

}
