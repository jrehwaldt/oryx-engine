package de.hpi.oryxengine.worklist.gui;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import de.hpi.oryxengine.exception.DalmatinaException;
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
    
   @Nonnull List<WorklistItem> getWorklistItems(@Nonnull ResourceType resourceType,
                                                @Nonnull UUID resourceId);
   
   void claimWorklistItemBy(@Nonnull UUID worklistItemId,
                            @Nonnull ResourceType resourceType,
                            @Nonnull UUID resourceId)
   throws DalmatinaException;
   
   void beginWorklistItemBy(@Nonnull UUID worklistItemId,
                            @Nonnull ResourceType resourceType,
                            @Nonnull UUID resourceId)
   throws DalmatinaException;
   
   void completeWorklistItemBy(@Nonnull UUID worklistItemId,
                               @Nonnull ResourceType resourceType,
                               @Nonnull UUID resourceId)
   throws DalmatinaException;
   
   void abortWorklistItemBy(@Nonnull UUID worklistItemId,
                            @Nonnull ResourceType resourceType,
                            @Nonnull UUID resourceId)
   throws DalmatinaException;
}
