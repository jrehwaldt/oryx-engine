package org.jodaengine.resource.allocation.pattern.detour;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.allocation.DetourPattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistServiceIntern;

/**
 * The Class Delegation Pattern. It allows to re-distribute an item to a specific participant.
 */
public class SimpleDelegationPattern implements DetourPattern {

    @Override
    public void distributeItem(WorklistServiceIntern worklistService,
                               AbstractWorklistItem worklistItem,
                               AbstractResource<?> oldResource,
                               AbstractResource<?> newResource) {

        if (newResource == null) {
            throw new JodaEngineRuntimeException(
                "When delegating an item, please ensure that there is a target resource!");
        } else {
            worklistService.removeWorklistItem(worklistItem, oldResource);
            worklistService.addWorklistItem(worklistItem, newResource);
        }
    }
}
