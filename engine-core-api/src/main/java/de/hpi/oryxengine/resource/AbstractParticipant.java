package de.hpi.oryxengine.resource;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.resource.worklist.WorklistItem;

/**
 * A Participants is a resource which a task can be assigned to.
 * 
 * For example, a clerk, but even a manager, etc.
 * 
 * @author Gerardo Navarro Suarez
 */
public abstract class AbstractParticipant extends AbstractResource<AbstractParticipant> {

    /**
     * The Default Constructor. Creates an {@link AbstractParticipant} with the given id.
     * 
     * @param resourceID
     *            - the resource id
     * @param participantName
     *            - identifier for the participant Object
     */
    public AbstractParticipant(@Nonnull UUID resourceID, String participantName) {

        super(resourceID, participantName, ResourceType.PARTICIPANT);
    }

    /**
     * The Default Constructor. Creates an {@link AbstractParticipant} with the given id.
     * 
     * @param participantName
     *            - identifier for the participant Object
     */
    public AbstractParticipant(String participantName) {

        super(participantName, ResourceType.PARTICIPANT);
    }
    
    /**
     * Returns a Set of all {@link AbstractPosition}s of this Participant.
     * 
     * @return a Set of all {@link AbstractPosition}s of this Participant
     */
    public abstract Set<AbstractPosition> getMyPositionsImmutable();

    // Participant addMyPosition(Position position);

    /**
     * Returns a read-only Set of all Capabilities of this Participant.
     * 
     * @return a read-only Set of all Capabilities of this Participant
     */
    public abstract Set<AbstractCapability> getMyCapabilities();
    // Participant addMyCapability(Capability capability);

    /**
     * Returns a read-only Set of all {@link AbstractRole}s that contains this Participant.
     * 
     * @return a read-only Set of all {@link AbstractRole}s that contains this Participant
     */
    public abstract Set<AbstractRole> getMyRolesImmutable();
    
    /**
     * Gets the worklist items which are currently in work(claimed) by this participant.
     *
     * @return the worklist items currently in work (claimed)
     */
    public abstract List<WorklistItem> getWorklistItemsCurrentlyInWork();
}
