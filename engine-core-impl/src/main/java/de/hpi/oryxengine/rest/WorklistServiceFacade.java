package de.hpi.oryxengine.rest;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.hpi.oryxengine.resource.ResourceType;
import de.hpi.oryxengine.resource.worklist.WorklistItem;

/**
 * This interface if a simpler version of the {@link WorklistService} interface.
 * This is needed, because resource serialization is avoided here and easier support for
 * Jersey and JAX-B is possible.
 * 
 * @author Jan Rehwaldt
 */
public interface WorklistServiceFacade {
    
   /**
    * Returns a list of work items for a certain resource.
    * 
    * @param resourceType the resource's type
    * @param resourceId the resource's id
    * @return the work items
    */
   @Nullable List<WorklistItem> getWorklistItems(@Nonnull ResourceType resourceType,
                                                 @Nonnull String resourceId);
   
   /**
    * Claims a certain work item.
    * 
    * @param worklistItemId the work item's id
    * @param resourceType the resource's type
    * @param resourceId the resource's id
    */
   void claimWorklistItemBy(@Nonnull String worklistItemId,
                            @Nonnull ResourceType resourceType,
                            @Nonnull String resourceId);
   
   /**
    * Begins a certain work item.
    * 
    * @param worklistItemId the work item's id
    * @param resourceType the resource's type
    * @param resourceId the resource's id
    */
   void beginWorklistItemBy(@Nonnull String worklistItemId,
                            @Nonnull ResourceType resourceType,
                            @Nonnull String resourceId);
   
   /**
    * Completes a certain work item.
    * 
    * @param worklistItemId the work item's id
    * @param resourceType the resource's type
    * @param resourceId the resource's id
    */
   void completeWorklistItemBy(@Nonnull String worklistItemId,
                               @Nonnull ResourceType resourceType,
                               @Nonnull String resourceId);
   
   /**
    * Aborts a certain work item.
    * 
    * @param worklistItemId the work item's id
    * @param resourceType the resource's type
    * @param resourceId the resource's id
    */
   void abortWorklistItemBy(@Nonnull String worklistItemId,
                            @Nonnull ResourceType resourceType,
                            @Nonnull String resourceId);
}
