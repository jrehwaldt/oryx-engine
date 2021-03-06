package org.jodaengine.eventmanagement.processevent.incoming;

import org.jodaengine.eventmanagement.subscription.IncomingProcessEvent;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.definition.ProcessDefinitionID;

/**
 * The Interface StartEvent. A start event, in contrast to the intermediate event, will trigger the instantiation of a
 * process.
 */
public interface IncomingStartProcessEvent extends IncomingProcessEvent {

    /**
     * Gets the assigned process definition that is to be instantiated upon event occurrence.
     * 
     * @return the definition id
     */
    ProcessDefinitionID getProcessDefinitionID();
    
    /**
     * Inject the navigator service needed to start an instance.
     * @param navigator the Navigator
     */
    void injectNavigatorService(Navigator navigator);
}
