package de.hpi.oryxengine.worklist;

import java.util.List;

public interface Worklist {

    List<WorklistItem> getWorklistItems();
    
    void itemIsAllocated(WorklistItem worklistItem);
    
    void addWorklistItem(WorklistItem worklistItem)
}
