package de.hpi.oryxengine.resource.worklist;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.exception.OryxEngineException;
import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.resource.Role;
import de.hpi.oryxengine.worklist.WorklistItem;

public class RoleWorklist extends AbstractWorklist {

    Role relatedRole;
    
    public RoleWorklist(Role owner) {

        this.relatedRole = owner;
    }
    
    @Override
    public List<WorklistItem> getWorklistItems() {

        List<WorklistItem> worklistItems = new ArrayList<WorklistItem>(getLazyWorklistItems());
        
        if (relatedRole.getSuperRole() != null) {
            
            worklistItems.addAll(relatedRole.getSuperRole().getWorklist().getWorklistItems());
        }
        
        return worklistItems;
    }

    @Override
    public void itemIsAllocatedBy(WorklistItem worklistItem, Resource<?> claimingResource)
    throws OryxEngineException {

        
        System.out.println("worklistitem: " + worklistItem);
        System.out.println("My List: " + getLazyWorklistItems());
        getLazyWorklistItems().remove(worklistItem);
        worklistItem.getAssignedResources().remove(relatedRole);
        System.out.println("My List: " + getLazyWorklistItems());
        System.out.println("My whole list: " + getWorklistItems());
    }

    @Override
    public void addWorklistItem(WorklistItem worklistItem)
    throws OryxEngineException {

        getLazyWorklistItems().add(worklistItem);
        worklistItem.getAssignedResources().add(relatedRole);
    }

    @Override
    public void itemIsCompleted(WorklistItem worklistItem)
    throws OryxEngineException {

        String exceptionMessage = "WorklistItems in a RoleWorklist can nor be executed neither be completed.";
        throw new OryxEngineException(exceptionMessage);        
    }

    @Override
    public void itemIsStarted(WorklistItem worklistItem)
    throws OryxEngineException {

        String exceptionMessage = "WorklistItems in a RoleWorklist can nor be executed neither be completed.";
        throw new OryxEngineException(exceptionMessage);        
    }

}
