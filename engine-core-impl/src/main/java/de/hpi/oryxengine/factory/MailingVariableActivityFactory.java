package de.hpi.oryxengine.factory;

import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.MailingVariable;

/**
 * A factory for creating MailingVariableActivity objects.
 */
public class MailingVariableActivityFactory extends AbstractActivityFactory {
    /**
     * Creates the Mailing Variable Activity.
     *
     * @return the activity
     */
    public static Activity create() {
        return new MailingVariable("result");
    }
}
