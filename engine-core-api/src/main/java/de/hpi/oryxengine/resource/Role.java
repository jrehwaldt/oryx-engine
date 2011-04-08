package de.hpi.oryxengine.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.hpi.oryxengine.resource.worklist.AbstractWorklist;
import de.hpi.oryxengine.resource.worklist.RoleWorklist;

/**
 * Implementation of the {@link RoleImpl} interface.
 * 
 * @author Gerardo Navarro Suarez
 * @author Jan Rehwaldt
 */
public class Role extends AbstractResource<Role> {

    /** The participants. */
    private Set<Participant> participants;

    /**
     * The Default Constructor for the RoleImpl.
     * 
     * @param roleName
     *            - the name of the Role
     */
    public Role(String roleName) {

        super(roleName, ResourceType.ROLE);
    }

    /**
     * Returns the superior Role of the current Role.
     * 
     * @return the superior Role of the current Role
     */
    public @Nullable Role getSuperRole() {

        return null; // TODO ever null
    }

    /**
     * Returns a read-only Set of all participants belonging to that Role.
     * 
     * @return a read-only Set of all participants belonging to that Role
     */
    public @Nonnull Set<Participant> getParticipantsImmutable() {

        Set<Participant> setToReturn = new HashSet<Participant>(getParticipants());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Gets the participant implementations.
     *
     * @return the participant implementations
     */
    protected @Nonnull Set<Participant> getParticipants() {

        if (participants == null) {
            participants = new HashSet<Participant>();
        }
        return participants;
    }
    
    @Override
    public AbstractWorklist getWorklist() {

        if (worklist == null) {

            worklist = new RoleWorklist(this);
        }
        return worklist;
    }

}
