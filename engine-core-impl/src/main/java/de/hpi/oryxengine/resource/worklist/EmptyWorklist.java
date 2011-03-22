package de.hpi.oryxengine.resource.worklist;

import java.util.Collections;
import java.util.List;

import de.hpi.oryxengine.worklist.WorklistItem;

/**
 * 
 */
public class EmptyWorklist extends AbstractWorklist {

    @Override
    public List<WorklistItem> getWorklistItems() {

        return Collections.emptyList();
    }

    @Override
    public void itemHasChanged(WorklistItem worklistItem) {

        // Do nothing
     }

}
