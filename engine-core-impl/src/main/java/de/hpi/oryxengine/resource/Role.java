package de.hpi.oryxengine.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.resource.worklist.AbstractWorklist;
import de.hpi.oryxengine.resource.worklist.RoleWorklist;

/**
 * Implementation of the {@link RoleImpl} interface.
 * 
 * @author Gerardo Navarro Suarez
 * @author Jan Rehwaldt
 */
public class Role extends AbstractRole {

    private Set<Participant> participants;
    
    /**
     * Hidden constructor.
     */
    protected Role() { }
    
    /**
     * The Default Constructor for the RoleImpl.
     * 
     * @param roleName
     *            - the name of the Role
     */
    public Role(String roleName) {

        super(roleName);
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
    public @Nonnull Set<AbstractParticipant> getParticipantsImmutable() {

        HashSet<AbstractParticipant> setToReturn = new HashSet<AbstractParticipant>(getParticipants());
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

    /**
     * Translates a Role into a corresponding RoleImpl object.
     *
     * Furthermore some constrains are checked.
     *
     * @param roleId
     *            - a Role object
     * @return role - the casted {@link Role}
     */
    public static Role asRoleImpl(UUID roleId) {
 
        if (roleId == null) {
            throw new DalmatinaRuntimeException("The Role parameter is null.");
        }

        Role roleImpl = (Role) ServiceFactory.getIdentityService().getRole(roleId);
        
        if (roleImpl == null) {
            throw new DalmatinaRuntimeException("There exists no Role with the id " + roleId + ".");
        }
        return roleImpl;
    }
}
