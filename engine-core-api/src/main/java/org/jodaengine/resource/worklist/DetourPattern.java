package org.jodaengine.resource.worklist;

import org.jodaengine.resource.AbstractResource;

/**
 * The Interface DetourPattern. Such a pattern, deriving from this interface, handles the way of a
 * {@link WorklistItemImpl Worklist Item} when it is canceled, delegated, reallocated or deallocated
 */
public interface DetourPattern {

    /**
     * Execute the pattern and do the desired routing work.
     *
     * @param worklistItem the item to reallocate, delegate or such
     * @param resource the resource to distribute the item to
     */
    void distribute(AbstractWorklistItem worklistItem, AbstractResource<?> resource);
}
