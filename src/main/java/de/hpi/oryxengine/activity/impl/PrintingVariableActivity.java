package de.hpi.oryxengine.activity.impl;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.processInstance.ProcessInstance;

public class PrintingVariableActivity implements Activity {

  private String variableName;

  public PrintingVariableActivity(String variableToBePrinted) {
    variableName = variableToBePrinted;
  }

  public void execute(ProcessInstance instance) {
    String variableValue = (String) instance.getVariable(variableName);
    System.out.println("In der Variable " + variableName + " steht " + variableValue + " .");
  }

}
