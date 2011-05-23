package org.jodaengine.resource.worklist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.AbstractRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The work list for the resource 'role'.
 */
public class RoleWorklist extends AbstractDefaultWorklist {

    @JsonIgnore
    private AbstractRole relatedRole;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * Hidden constructor.
     */
    protected RoleWorklist() { }
    
    /**
     * Instantiates a new role worklist.
     *
     * @param owner the owner
     */
    public RoleWorklist(AbstractRole owner) {

        this.relatedRole = owner;
    }
    
    @Override
    public List<AbstractWorklistItem> getWorklistItems() {

        // a role only has offered worklist items
        List<AbstractWorklistItem> worklistItems = new ArrayList<AbstractWorklistItem>(getLazyWorklistItems());
        
        if (relatedRole.getSuperRole() != null) {
            worklistItems.addAll(relatedRole.getSuperRole().getWorklist().getWorklistItems());
        }
        
        return Collections.unmodifiableList(worklistItems);
    }

    @Override
    public void itemIsAllocatedBy(AbstractWorklistItem worklistItem, AbstractResource<?> claimingResource) {

        
        logger.debug("worklistitem: {}", worklistItem);
        logger.debug("My List: {}", getLazyWorklistItems());
        getLazyWorklistItems().remove(worklistItem);
        worklistItem.getAssignedResources().remove(relatedRole);
        logger.debug("My List: {}", getLazyWorklistItems());
        logger.debug("My whole list: {}", getWorklistItems());
    }

    @Override
    public synchronized void addWorklistItem(AbstractWorklistItem worklistItem) {

        getLazyWorklistItems().add(worklistItem);
        worklistItem.getAssignedResources().add(relatedRole);
          
    }
    

    @Override
    public void removeWorklistItem(AbstractWorklistItem worklistItem) {

        getLazyWorklistItems().remove(worklistItem);
        worklistItem.getAssignedResources().remove(relatedRole);
        
    }

    @Override
    public void itemIsCompleted(AbstractWorklistItem worklistItem) {

        String exceptionMessage = "WorklistItems in a RoleWorklist can be neither executed nor completed.";
        throw new JodaEngineRuntimeException(exceptionMessage);        
    }

    @Override
    public void itemIsStarted(AbstractWorklistItem worklistItem) {

        String exceptionMessage = "WorklistItems in a RoleWorklist can be neither executed nor completed.";
        throw new JodaEngineRuntimeException(exceptionMessage);        
    }

}
