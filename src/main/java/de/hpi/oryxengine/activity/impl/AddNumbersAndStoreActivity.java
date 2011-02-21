package de.hpi.oryxengine.activity.impl;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.processInstance.ProcessInstance;

public class AddNumbersAndStoreActivity implements Activity {

  private int numberA;
  private int numberB;
  private String resultVariableName;

  public AddNumbersAndStoreActivity(int a, int b, String varibaleName) {
    numberA = a;
    numberB = b;
    resultVariableName = varibaleName;
  }

  public void execute(ProcessInstance instance) {
    int result = numberA + numberB;
    instance.setVariable(resultVariableName, "" + result);
  }

}
