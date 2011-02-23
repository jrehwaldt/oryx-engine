package de.hpi.oryxengine.navigator.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.hpi.oryxengine.processInstance.ProcessInstance;
import de.hpi.oryxengine.processstructure.Transition;
import de.hpi.oryxengine.processstructure.impl.NodeImpl;

public class NavigationThread extends Thread {

  private List<ProcessInstance> toNavigate;
  private Logger logger = Logger.getRootLogger();
  public boolean shouldStop = false;

  public NavigationThread(String threadname, List<ProcessInstance> activityQueue) {
    super(threadname);
    this.toNavigate = activityQueue;
  }
  public void run() {
    doWork();
  }

  // Main Loop: Takes a executable process instance and the belonging node
  // and executes the node. After, the true conditions are followed and the next
  // node is set.
  // Now the process instance is added to the Queue again. This has the
  // advantage that the navigator can now
  // handle multiple instances.

  public void doWork() {

    while (true) {
      // Das muss auf jeden fall verändert werden
      if (shouldStop ) {
        break;
      }
      
      ProcessInstance instance = null;
      
      // This has to be an atomic operation on toNavigate, otherwise
      // an IndexOutOfBoundsException might occur
      synchronized(this.toNavigate) {
        if (this.toNavigate.size() > 0) {
          instance = this.toNavigate.remove(0);
        }
      }
      
      if (instance != null) {
        List<ProcessInstance> instances = instance.executeStep();
        toNavigate.addAll(instances);        
      } else {
        try {
          logger.debug("Queue empty");
          sleep(1000);

        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
