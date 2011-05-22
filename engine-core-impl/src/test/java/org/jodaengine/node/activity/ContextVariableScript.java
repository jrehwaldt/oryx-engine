package org.jodaengine.node.activity;

import org.jodaengine.node.activity.custom.AbstractJodaScript;
import org.jodaengine.process.instance.ProcessInstanceContext;

/**
 * The Class ContextVariableScript. This is for test purposes only.
 */
public class ContextVariableScript extends AbstractJodaScript {

    /**
     * This is a test script that can be used for a ScriptActivity. It does not override the super-method, as this is
     * not possible for statics.
     * 
     * @param context
     *            the context
     */
    public static void execute(ProcessInstanceContext context) {

        context.setVariable("scriptVariable", "set");

    }

}
