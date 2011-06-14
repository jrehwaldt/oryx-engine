package org.jodaengine.resource.allocation;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemImpl;

/**
 * The Interface DetourPattern. Such a pattern, deriving from this interface, handles the way of a
 * {@link WorklistItemImpl Worklist Item} when it is canceled, delegated, reallocated or deallocated
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public interface DetourPattern {

    /**
     * Execute the pattern and do the desired routing work.
     *
     * @param worklistItem the item to reallocate, delegate or such
     * @param resource the resource to distribute the item to
     */
    void distribute(AbstractWorklistItem worklistItem, AbstractResource<?> resource);
}
