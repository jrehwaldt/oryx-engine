package de.hpi.oryxengine.resource.worklist;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.resource.Role;
import de.hpi.oryxengine.worklist.WorklistItem;

/**
 * The work list for the resource 'role'.
 */
public class RoleWorklist extends AbstractWorklist {

    Role relatedRole;
    
    /**
     * Instantiates a new role worklist.
     *
     * @param owner the owner
     */
    public RoleWorklist(Role owner) {

        this.relatedRole = owner;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<WorklistItem> getWorklistItems() {

        List<WorklistItem> worklistItems = new ArrayList<WorklistItem>(getLazyWorklistItems());
        
        if (relatedRole.getSuperRole() != null) {
            
            worklistItems.addAll(relatedRole.getSuperRole().getWorklist().getWorklistItems());
        }
        
        return worklistItems;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void itemIsAllocatedBy(WorklistItem worklistItem, Resource<?> claimingResource)
    throws DalmatinaException {

        
        System.out.println("worklistitem: " + worklistItem);
        System.out.println("My List: " + getLazyWorklistItems());
        getLazyWorklistItems().remove(worklistItem);
        worklistItem.getAssignedResources().remove(relatedRole);
        System.out.println("My List: " + getLazyWorklistItems());
        System.out.println("My whole list: " + getWorklistItems());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWorklistItem(WorklistItem worklistItem)
    throws DalmatinaException {

        getLazyWorklistItems().add(worklistItem);
        worklistItem.getAssignedResources().add(relatedRole);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void itemIsCompleted(WorklistItem worklistItem)
    throws DalmatinaException {

        String exceptionMessage = "WorklistItems in a RoleWorklist can nor be executed neither be completed.";
        throw new DalmatinaException(exceptionMessage);        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void itemIsStarted(WorklistItem worklistItem)
    throws DalmatinaException {

        String exceptionMessage = "WorklistItems in a RoleWorklist can nor be executed neither be completed.";
        throw new DalmatinaException(exceptionMessage);        
    }

}
