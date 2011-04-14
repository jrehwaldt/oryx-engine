package de.hpi.oryxengine.resource.worklist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.AbstractRole;

/**
 * The work list for the resource 'role'.
 */
public class RoleWorklist extends AbstractDefaultWorklist {

    @JsonIgnore
    private AbstractRole relatedRole;
    
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
    public List<WorklistItem> getWorklistItems() {

        List<WorklistItem> worklistItems = new ArrayList<WorklistItem>(getLazyWorklistItems());
        
        if (relatedRole.getSuperRole() != null) {
            worklistItems.addAll(relatedRole.getSuperRole().getWorklist().getWorklistItems());
        }
        
        return Collections.unmodifiableList(worklistItems);
    }

    @Override
    public void itemIsAllocatedBy(WorklistItem worklistItem, AbstractResource<?> claimingResource) {

        
        System.out.println("worklistitem: " + worklistItem);
        System.out.println("My List: " + getLazyWorklistItems());
        getLazyWorklistItems().remove(worklistItem);
        worklistItem.getAssignedResources().remove(relatedRole);
        System.out.println("My List: " + getLazyWorklistItems());
        System.out.println("My whole list: " + getWorklistItems());
    }

    @Override
    public void addWorklistItem(WorklistItem worklistItem) {

        getLazyWorklistItems().add(worklistItem);
        worklistItem.getAssignedResources().add(relatedRole);
    }

    @Override
    public void itemIsCompleted(WorklistItem worklistItem) {

        String exceptionMessage = "WorklistItems in a RoleWorklist can nor be executed neither be completed.";
        throw new DalmatinaRuntimeException(exceptionMessage);        
    }

    @Override
    public void itemIsStarted(WorklistItem worklistItem) {

        String exceptionMessage = "WorklistItems in a RoleWorklist can nor be executed neither be completed.";
        throw new DalmatinaRuntimeException(exceptionMessage);        
    }

}
