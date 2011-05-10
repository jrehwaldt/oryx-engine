package de.hpi.oryxengine.resource.worklist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractPosition;
import de.hpi.oryxengine.resource.AbstractResource;

/**
 * Worklist for a participant.
 */
public class ParticipantWorklist extends AbstractDefaultWorklist {
    
    private AbstractParticipant relatedParticipant;
    
    /**
     * Hidden constructor.
     */
    protected ParticipantWorklist() { }
    
    /**
     * Instantiates a new participant worklist.
     *
     * @param owner the related participant to the work list 
     */
    public ParticipantWorklist(@Nonnull AbstractParticipant owner) {
        
        this.relatedParticipant = owner;
    }
    
    /**
     * Gets the resources in view. These are all roles, positions, etc. this participant belongs to.
     *
     * @return the resources in view
     */
    private List<AbstractResource<?>> getResourcesInView() {
        List<AbstractResource<?>> resourcesInView = new ArrayList<AbstractResource<?>>();
        resourcesInView.addAll(relatedParticipant.getMyRolesImmutable());
        for (AbstractPosition position: relatedParticipant.getMyPositionsImmutable()) {
            resourcesInView.add(position);
            resourcesInView.add(position.belongstoOrganization());
        }
        
        return resourcesInView;
    }

    @Override
    public List<AbstractWorklistItem> getAllWorklistItems() {
        
        // this is needed for deserialization to not break the circular dependency
        if (relatedParticipant == null) {
            return Collections.emptyList();
        }
        
        // Extracting the resources related to this owner
        List<AbstractResource<?>> resourcesInView = getResourcesInView();

        // Creating the list of worklistItems from the owner and the related resources
        List<AbstractWorklistItem> resultWorklistItems = new ArrayList<AbstractWorklistItem>();
        resultWorklistItems.addAll(getLazyOfferedWorklistItems());
        resultWorklistItems.addAll(getLazyAllocatedWorklistItems());
        resultWorklistItems.addAll(getLazyExecutingWorklistItems());
        for (AbstractResource<?> resourceInView : resourcesInView) {
            resultWorklistItems.addAll(resourceInView.getWorklist().getAllWorklistItems());
        }

        return resultWorklistItems;
    }

    @Override
    public void itemIsAllocatedBy(AbstractWorklistItem worklistItem, AbstractResource<?> claimingResource) {

        WorklistItemImpl worklistItemImpl = WorklistItemImpl.asWorklistItemImpl(worklistItem);

        if (claimingResource.equals(relatedParticipant)) {

            if (!getLazyAllocatedWorklistItems().contains(worklistItemImpl)) {

                addWorklistItem(worklistItemImpl);
            }

            worklistItemImpl.setStatus(WorklistItemState.ALLOCATED);
        } else {
            // we assume, that when an item has been allocated, it must have been offered before
            getLazyOfferedWorklistItems().remove(worklistItemImpl);
            
            worklistItemImpl.getAssignedResources().remove(relatedParticipant);
        }
    }

    @Override
    public synchronized void addWorklistItem(AbstractWorklistItem worklistItem) {

        switch (worklistItem.getStatus()) {
            case ALLOCATED:
                getLazyAllocatedWorklistItems().add(worklistItem);
                break;
            case OFFERED:
                getLazyOfferedWorklistItems().add(worklistItem);
                break;
            case EXECUTING:
                getLazyExecutingWorklistItems().add(worklistItem);
                break;
            default:
                // maybe do error handling here
                break;
        }
        worklistItem.getAssignedResources().add(relatedParticipant);
    }

    @Override
    public void itemIsCompleted(AbstractWorklistItem worklistItem) {

        WorklistItemImpl worklistItemImpl = WorklistItemImpl.asWorklistItemImpl(worklistItem);

        worklistItemImpl.setStatus(WorklistItemState.COMPLETED);

        // we assume that the item have been 'executing' before
        getLazyExecutingWorklistItems().remove(worklistItemImpl);
    }

    @Override
    public void itemIsStarted(AbstractWorklistItem worklistItem) {

        WorklistItemImpl worklistItemImpl = WorklistItemImpl.asWorklistItemImpl(worklistItem);
        worklistItemImpl.setStatus(WorklistItemState.EXECUTING);
    }

    @Override
    public List<AbstractWorklistItem> getAllocatedWorklistItems() {

        if (relatedParticipant == null) {
            return Collections.emptyList();
        }
        return getLazyAllocatedWorklistItems();
    }

    @Override
    public List<AbstractWorklistItem> getOfferedWorklistItems() {

        // this is needed for deserialization to not break the circular dependency
        if (relatedParticipant == null) {
            return Collections.emptyList();
        }
        
        // Extracting the resources related to this owner
        List<AbstractResource<?>> resourcesInView = getResourcesInView();

        // Creating the list of worklistItems from the owner and the related resources
        List<AbstractWorklistItem> resultWorklistItems = new ArrayList<AbstractWorklistItem>();
        resultWorklistItems.addAll(getLazyOfferedWorklistItems());
        for (AbstractResource<?> resourceInView : resourcesInView) {
            resultWorklistItems.addAll(resourceInView.getWorklist().getOfferedWorklistItems());
        }

        return resultWorklistItems;
    }

    @Override
    public List<AbstractWorklistItem> getExecutingWorklistItems() {

        // this is needed for deserialization to not break the circular dependency
        if (relatedParticipant == null) {
            return Collections.emptyList();
        }
        
        return getLazyExecutingWorklistItems();
    }

}
