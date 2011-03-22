package de.hpi.oryxengine.resource.worklist;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Position;
import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.worklist.WorklistItem;
import de.hpi.oryxengine.worklist.WorklistItemImpl;
import de.hpi.oryxengine.worklist.WorklistItemState;

public class ParticipantWorklist extends AbstractWorklist {

    Participant owner;
    
    public ParticipantWorklist(Participant owner) {

        this.owner = owner;
    }

    @Override
    public List<WorklistItem> getWorklistItems() {

        // Extracting the resources related to this owner
        List<Resource<?>> resourcesInView = new ArrayList<Resource<?>>();
        resourcesInView.addAll(owner.getMyRoles());
        for (Position position : owner.getMyPositions()) {
            resourcesInView.add(position);
            resourcesInView.add(position.belongstoOrganization());
        }

        // Creating the list of worklistItems from the owner and the related resources
        List<WorklistItem> resultWorklistItems = new ArrayList<WorklistItem>();
        resultWorklistItems.addAll(getLazyWorklistItems());
        for (Resource<?> resourceInView : resourcesInView) {
            resultWorklistItems.addAll(resourceInView.getWorklist().getWorklistItems());
        }

        return resultWorklistItems;
    }

    
    
    @Override
    public void itemHasChanged(WorklistItem worklistItem) {

        // TODO Auto-generated method stub

    }

    @Override
    public void itemIsAllocated(WorklistItem worklistItem) {

        WorklistItemImpl worklistItemImpl = WorklistItemImpl.asWorklistItemImpl(worklistItem);
        
        worklistItemImpl.setStatus(WorklistItemState.ALLOCATED);
        
    }

    @Override
    public void addWorklistItem(WorklistItem worklistItem) {
        worklistItem.getAssignedResources().add(owner);
    }

}
