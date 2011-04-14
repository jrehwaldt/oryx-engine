package de.hpi.oryxengine.resource.worklist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Represents a DefaultWorklist that contains several {@link WorklistItem} for a certain {@link AbstractResource}.
 */
public abstract class AbstractDefaultWorklist extends AbstractWorklist {
    
    private List<WorklistItem> lazyWorklistItems;
    
    /**
     * Retrieves the {@link WorklistItem}s that are in this {@link Worklist}.
     * 
     * @return a list of {@link WorklistItem}s; the list is instantiated on demand
     */
    @JsonProperty
    public List<WorklistItem> getLazyWorklistItems() {
        
        if (lazyWorklistItems == null) {
            lazyWorklistItems = new ArrayList<WorklistItem>();
        }
        
        return lazyWorklistItems;
    }
    
    @Override
    public Iterator<WorklistItem> iterator() {
        return getWorklistItems().iterator();
    }
}
