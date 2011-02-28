package de.hpi.oryxengine.factory;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.PrintingVariableActivity;

/**
 * A factory for creating PrintingVariableActivity objects.
 */
public class PrintingVariableActivityFactory extends AbstractActivityFactory {
    /**
     * Creates the Printing Variable Activity.
     *
     * @return the node
     */
    public static Activity create() {
        return new PrintingVariableActivity("result");
    }

}
