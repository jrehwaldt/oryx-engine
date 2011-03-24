package de.hpi.oryxengine.resource.worklist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hpi.oryxengine.allocation.WorklistItemImpl;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Position;
import de.hpi.oryxengine.resource.Resource;

/**
 * 
 */
public class ParticipantWorklist extends AbstractWorklist {

    private Participant relatedParticipant;

    public ParticipantWorklist(Participant owner) {

        this.relatedParticipant = owner;
    }

    @Override
    public List<WorklistItem> getWorklistItems() {

        // Extracting the resources related to this owner
        List<Resource<?>> resourcesInView = new ArrayList<Resource<?>>();
        resourcesInView.addAll(relatedParticipant.getMyRoles());
        for (Position position : relatedParticipant.getMyPositions()) {
            resourcesInView.add(position);
            resourcesInView.add(position.belongstoOrganization());
        }

        // Creating the list of worklistItems from the owner and the related resources
        List<WorklistItem> resultWorklistItems = new ArrayList<WorklistItem>();
        resultWorklistItems.addAll(getLazyWorklistItems());
        for (Resource<?> resourceInView : resourcesInView) {
            resultWorklistItems.addAll(resourceInView.getWorklist().getWorklistItems());
        }

        return Collections.unmodifiableList(resultWorklistItems);
    }

    @Override
    public void itemIsAllocatedBy(WorklistItem worklistItem, Resource<?> claimingResource) {

        WorklistItemImpl worklistItemImpl = WorklistItemImpl.asWorklistItemImpl(worklistItem);

        if (claimingResource.equals(relatedParticipant)) {

            if (!getLazyWorklistItems().contains(worklistItemImpl)) {

                addWorklistItem(worklistItemImpl);
            }

            worklistItemImpl.setStatus(WorklistItemState.ALLOCATED);
        } else {

            getLazyWorklistItems().remove(worklistItemImpl);
            worklistItemImpl.getAssignedResources().remove(relatedParticipant);
        }
    }

    @Override
    public void addWorklistItem(WorklistItem worklistItem) {

        getLazyWorklistItems().add(worklistItem);
        worklistItem.getAssignedResources().add(relatedParticipant);
    }

    @Override
    public void itemIsCompleted(WorklistItem worklistItem) {

        WorklistItemImpl worklistItemImpl = WorklistItemImpl.asWorklistItemImpl(worklistItem);

        worklistItemImpl.setStatus(WorklistItemState.COMPLETED);

        getLazyWorklistItems().remove(worklistItemImpl);
    }

    @Override
    public void itemIsStarted(WorklistItem worklistItem) {

        WorklistItemImpl worklistItemImpl = WorklistItemImpl.asWorklistItemImpl(worklistItem);
        worklistItemImpl.setStatus(WorklistItemState.EXECUTING);
    }

}
