package org.jodaengine.node.activity;

import org.jodaengine.node.activity.bpmn.AbstractJavaTask;
import org.jodaengine.process.instance.ProcessInstanceContext;

/**
 * The Class ContextVariableJavaTask. This is for test purposes only.
 */
public class ContextVariableJavaTask extends AbstractJavaTask {

    /**
     * This is a test java class that can be used for a Java Service Task. It does not override the super-method, as
     * this is not possible for statics.
     * 
     * @param context
     *            the context
     */
    public static void execute(ProcessInstanceContext context) {

        context.setVariable("serviceVariable", "set");

    }

}
