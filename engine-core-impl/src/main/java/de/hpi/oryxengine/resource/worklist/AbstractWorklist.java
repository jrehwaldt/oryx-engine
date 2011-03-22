package de.hpi.oryxengine.resource.worklist;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.worklist.Worklist;
import de.hpi.oryxengine.worklist.WorklistItem;

public abstract class AbstractWorklist implements Worklist {

    private List<WorklistItem> lazyWorklistItems;
    
    @Override
    public abstract List<WorklistItem> getWorklistItems();

    List<WorklistItem> getLazyWorklistItems() {

        if (lazyWorklistItems == null) {
            lazyWorklistItems = new ArrayList<WorklistItem>();
        }
        
        return lazyWorklistItems;
    }

}
