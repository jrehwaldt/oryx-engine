package de.hpi.oryxengine.resource.worklist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.exception.JodaEngineRuntimeException;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.AbstractRole;

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
    public List<AbstractWorklistItem> getAllWorklistItems() {

        // a role only has offered worklist items
        return getOfferedWorklistItems();
    }

    @Override
    public void itemIsAllocatedBy(AbstractWorklistItem worklistItem, AbstractResource<?> claimingResource) {

        
        logger.debug("worklistitem: {}", worklistItem);
        logger.debug("My List: {}", getLazyOfferedWorklistItems());
        getLazyOfferedWorklistItems().remove(worklistItem);
        worklistItem.getAssignedResources().remove(relatedRole);
        logger.debug("My List: {}", getLazyOfferedWorklistItems());
        logger.debug("My whole list: {}", getAllWorklistItems());
    }

    @Override
    public synchronized void addWorklistItem(AbstractWorklistItem worklistItem) {

        switch (worklistItem.getStatus()) {
            case OFFERED:
                getLazyOfferedWorklistItems().add(worklistItem);
                worklistItem.getAssignedResources().add(relatedRole);
                break;

            default:
                // TODO cannot add workitems that are in a state other than 'offered'. throw an error.
                break;
        }
        
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

    @Override
    public List<AbstractWorklistItem> getAllocatedWorklistItems() {

        // roles do not have allocated worklist items
        return Collections.emptyList();
    }

    @Override
    public List<AbstractWorklistItem> getOfferedWorklistItems() {

        // a role only has offered worklist items
        List<AbstractWorklistItem> worklistItems = new ArrayList<AbstractWorklistItem>(getLazyOfferedWorklistItems());
        
        if (relatedRole.getSuperRole() != null) {
            worklistItems.addAll(relatedRole.getSuperRole().getWorklist().getAllWorklistItems());
        }
        
        return Collections.unmodifiableList(worklistItems);
    }

    @Override
    public List<AbstractWorklistItem> getExecutingWorklistItems() {

        // roles do not have executing worklist items
        return Collections.emptyList();
    }

}
