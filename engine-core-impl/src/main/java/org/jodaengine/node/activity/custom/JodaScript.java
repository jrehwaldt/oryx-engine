package org.jodaengine.node.activity.custom;

import org.jodaengine.process.instance.ProcessInstanceContext;

/**
 * The Interface JodaScript provides methods for custom scripts to be executable. It is used in the ScriptingActivity.
 */
public interface JodaScript {

    /**
     * Executes the custom behaviour.
     * During execution the custom script has access to the ProcessInstanceContext.
     *
     * @param context the context
     */
    void execute(ProcessInstanceContext context);
}
