package org.jodaengine.node.activity.bpmn;

import org.jodaengine.process.instance.ProcessInstanceContext;

/**
 * The Abstract Class JodaScript provides methods for custom scripts to be executable. It is used in the
 * ScriptingActivity.
 * Implement this interface, if you want to implement a custom script. You need a publicly visible no-arguments
 * constructor.
 */
public abstract class AbstractJodaScript {

    /**
     * Executes the custom behaviour.
     * During execution the custom script has access to the ProcessInstanceContext.
     * You cannot override static methods, but you can declare them in any subclass again and thus hide this method from
     * the super-type.
     * 
     * @param context
     *            the context
     */
    public static void execute(ProcessInstanceContext context) {

    }
}
