package de.hpi.oryxengine.resource.worklist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Represents a DefaultWorklist that contains several {@link AbstractWorklistItem} for a certain
 * {@link AbstractResource}.
 */
public abstract class AbstractDefaultWorklist extends AbstractWorklist {

    private List<AbstractWorklistItem> lazyOfferedWorklistItems;
    private List<AbstractWorklistItem> lazyAllocatedWorklistItems;
    private List<AbstractWorklistItem> lazyExecutingWorklistItems;

    /**
     * Retrieves the {@link AbstractWorklistItem}s that are in this {@link Worklist} and in state 'offered'.
     * 
     * @return a list of {@link AbstractWorklistItem}s; the list is instantiated on demand
     */
    @JsonProperty
    public synchronized List<AbstractWorklistItem> getLazyOfferedWorklistItems() {

        if (lazyOfferedWorklistItems == null) {
            lazyOfferedWorklistItems = Collections.synchronizedList(new ArrayList<AbstractWorklistItem>());
        }

        return lazyOfferedWorklistItems;
    }
    
    /**
     * Retrieves the {@link AbstractWorklistItem}s that are in this {@link Worklist} and in state 'allocated'.
     * 
     * @return a list of {@link AbstractWorklistItem}s; the list is instantiated on demand
     */
    @JsonProperty
    public synchronized List<AbstractWorklistItem> getLazyAllocatedWorklistItems() {

        if (lazyAllocatedWorklistItems == null) {
            lazyAllocatedWorklistItems = Collections.synchronizedList(new ArrayList<AbstractWorklistItem>());
        }

        return lazyAllocatedWorklistItems;
    }
    
    /**
     * Retrieves the {@link AbstractWorklistItem}s that are in this {@link Worklist} and in state 'executing'.
     * 
     * @return a list of {@link AbstractWorklistItem}s; the list is instantiated on demand
     */
    @JsonProperty
    public synchronized List<AbstractWorklistItem> getLazyExecutingWorklistItems() {

        if (lazyExecutingWorklistItems == null) {
            lazyExecutingWorklistItems = Collections.synchronizedList(new ArrayList<AbstractWorklistItem>());
        }

        return lazyExecutingWorklistItems;
    }

    @Override
    public Iterator<AbstractWorklistItem> iterator() {

        return getAllWorklistItems().iterator();
    }
}
