package de.hpi.oryxengine.factory;

import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;

/**
 * A factory for creating AddNumberAndStoreNode objects.
 * It just sets the activity to the AddNumberAndStoreActivity, which is instructed to sum up 1 and 1.
 */
public class AddNumbersAndStoreNodeFactory extends AbstractNodeFactory {
    
    /**
     * Sets the activity, overwriting the process in the AbstractNodeFactory.
     */
    @Override
    public void setActivity() {
        activity = new AddNumbersAndStoreActivity("result", 1, 1);
    }

}
