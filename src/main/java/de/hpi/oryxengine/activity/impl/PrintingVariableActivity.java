package de.hpi.oryxengine.activity.impl;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.processInstance.ProcessInstance;

public class PrintingVariableActivity implements Activity {

  private String varibaleName;

  public PrintingVariableActivity(String variableToBePrinted) {
    varibaleName = variableToBePrinted;
  }

  public void execute(ProcessInstance instance) {
    System.out.println((String) instance.getVariable(varibaleName));
  }

}
