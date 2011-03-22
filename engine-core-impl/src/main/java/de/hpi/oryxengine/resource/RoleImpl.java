package de.hpi.oryxengine.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.hpi.oryxengine.resource.worklist.RoleWorklist;
import de.hpi.oryxengine.worklist.Worklist;

/**
 * Implementation of the {@link Role} interface.
 * 
 * @author Gerardo Navarro Suarez
 */
public class RoleImpl extends ResourceImpl<Role> implements Role {

    /** The participants. */
    private Set<ParticipantImpl> participants;

    /**
     * The Default Constructor for the Rolempl.
     * 
     * @param roleId
     *            - the id of the Role
     */
    public RoleImpl(String roleId) {

        super(roleId, ResourceType.ROLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Role getSuperRole() {

        return null;
    }

    // @Override
    // public Role setSuperRole(Role superRole) {
    //
    // return null;
    // }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Participant> getParticipants() {

        Set<Participant> setToReturn = new HashSet<Participant>(getParticipantImpls());
        return Collections.unmodifiableSet(setToReturn);
    }

    /**
     * Gets the participant implementations.
     *
     * @return the participant implementations
     */
    protected Set<ParticipantImpl> getParticipantImpls() {

        if (participants == null) {
            participants = new HashSet<ParticipantImpl>();
        }
        return participants;
    }
    
    @Override
    public Worklist getWorklist() {

        if (worklist == null) {

            worklist = new RoleWorklist();
        }
        return worklist;
    }

}
