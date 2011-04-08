package de.hpi.oryxengine.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.hpi.oryxengine.resource.worklist.RoleWorklist;
import de.hpi.oryxengine.resource.worklist.Worklist;

/**
 * Implementation of the {@link RoleImpl} interface.
 * 
 * @author Gerardo Navarro Suarez
 * @author Jan Rehwaldt
 */
public class RoleImpl extends AbstractResourceImpl<RoleImpl> {

    /** The participants. */
    private Set<ParticipantImpl> participants;

    /**
     * The Default Constructor for the RoleImpl.
     * 
     * @param roleName
     *            - the name of the Role
     */
    public RoleImpl(String roleName) {

        super(roleName, ResourceType.ROLE);
    }

    /**
     * Returns the superior Role of the current Role.
     * 
     * @return the superior Role of the current Role
     */
    public @Nullable RoleImpl getSuperRole() {

        return null; // TODO ever null
    }

    /**
     * Returns a read-only Set of all participants belonging to that Role.
     * 
     * @return a read-only Set of all participants belonging to that Role
     */
    public @Nonnull Set<ParticipantImpl> getParticipantsImmutable() {

        Set<ParticipantImpl> setToReturn = new HashSet<ParticipantImpl>(getParticipants());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Gets the participant implementations.
     *
     * @return the participant implementations
     */
    protected @Nonnull Set<ParticipantImpl> getParticipants() {

        if (participants == null) {
            participants = new HashSet<ParticipantImpl>();
        }
        return participants;
    }
    
    @Override
    public Worklist getWorklist() {

        if (worklist == null) {

            worklist = new RoleWorklist(this);
        }
        return worklist;
    }

}
