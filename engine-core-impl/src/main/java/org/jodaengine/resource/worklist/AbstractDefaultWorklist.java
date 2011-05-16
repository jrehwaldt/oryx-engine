package org.jodaengine.resource.worklist;

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

    private List<AbstractWorklistItem> lazyWorklistItems;

    /**
     * Retrieves the {@link AbstractWorklistItem}s that are in this {@link Worklist} and in state 'offered'.
     * 
     * @return a list of {@link AbstractWorklistItem}s; the list is instantiated on demand
     */
    @JsonProperty
    public synchronized List<AbstractWorklistItem> getLazyWorklistItems() {

        if (lazyWorklistItems == null) {
            lazyWorklistItems = Collections.synchronizedList(new ArrayList<AbstractWorklistItem>());
        }

        return lazyWorklistItems;
    }

    @Override
    public Iterator<AbstractWorklistItem> iterator() {

        return getWorklistItems().iterator();
    }
}
