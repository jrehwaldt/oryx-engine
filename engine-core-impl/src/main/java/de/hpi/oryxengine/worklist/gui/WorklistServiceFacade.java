package de.hpi.oryxengine.worklist.gui;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

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
   @Nonnull List<WorklistItem> getWorklistItems(@Nonnull ResourceType resourceType,
                                                @Nonnull UUID resourceId);
   
   /**
    * Claims a certain work item.
    * 
    * @param worklistItemId the work item's id
    * @param resourceType the resource's type
    * @param resourceId the resource's id
    */
   void claimWorklistItemBy(@Nonnull UUID worklistItemId,
                            @Nonnull ResourceType resourceType,
                            @Nonnull UUID resourceId);
   
   /**
    * Begins a certain work item.
    * 
    * @param worklistItemId the work item's id
    * @param resourceType the resource's type
    * @param resourceId the resource's id
    */
   void beginWorklistItemBy(@Nonnull UUID worklistItemId,
                            @Nonnull ResourceType resourceType,
                            @Nonnull UUID resourceId);
   
   /**
    * Completes a certain work item.
    * 
    * @param worklistItemId the work item's id
    * @param resourceType the resource's type
    * @param resourceId the resource's id
    */
   void completeWorklistItemBy(@Nonnull UUID worklistItemId,
                               @Nonnull ResourceType resourceType,
                               @Nonnull UUID resourceId);
   
   /**
    * Aborts a certain work item.
    * 
    * @param worklistItemId the work item's id
    * @param resourceType the resource's type
    * @param resourceId the resource's id
    */
   void abortWorklistItemBy(@Nonnull UUID worklistItemId,
                            @Nonnull ResourceType resourceType,
                            @Nonnull UUID resourceId);
}
