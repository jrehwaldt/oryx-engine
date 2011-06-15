package org.jodaengine.resource.allocation;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistServiceIntern;

/**
 * The Interface DetourPattern. Such a pattern, deriving from this interface, handles the way of a
 * {@link WorklistItemImpl Worklist Item} when it is canceled, delegated, reallocated or deallocated
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
public interface DetourPattern {

    /**
     * Execute the pattern and do the desired routing work.
     * 
     * @param worklistService
     *            the {@link WorklistServiceIntern Worklist Service} that manages the worklists of resources
     * @param worklistItem
     *            the item to reallocate, delegate or such
     * @param oldResource
     *            the old resource that triggered the execution of the pattern
     * @param newResource
     *            the new resource to route the item to (if left <code>null</code>, the pattern has to manage to find a
     *            resource to
     *            route the item to!)
     */
    void distributeItem(@Nonnull WorklistServiceIntern worklistService,
                        @Nonnull AbstractWorklistItem worklistItem,
                        @Nonnull AbstractResource<?> oldResource,
                        AbstractResource<?> newResource);
}
