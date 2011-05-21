package org.jodaengine.node.activity;

import org.jodaengine.node.activity.custom.JodaScript;
import org.jodaengine.process.instance.ProcessInstanceContext;

/**
 * The Class ContextVariableScript. This is for test purposes only.
 */
public class ContextVariableScript implements JodaScript {

    @Override
    public void execute(ProcessInstanceContext context) {

        context.setVariable("scriptVariable", "set");
        
    }

}
