package org.jodaengine.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jodaengine.ServiceFactory;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.resource.worklist.AbstractWorklist;
import org.jodaengine.resource.worklist.RoleWorklist;


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

        // TODO ever null
        return null;
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
    public synchronized AbstractWorklist getWorklist() {

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
     * @throws ResourceNotAvailableException 
     */
    public static Role asRoleImpl(UUID roleId) throws ResourceNotAvailableException {
 
        if (roleId == null) {
            throw new JodaEngineRuntimeException("The Role parameter is null.");
        }

        Role roleImpl = (Role) ServiceFactory.getIdentityService().getRole(roleId);
        
        if (roleImpl == null) {
            throw new JodaEngineRuntimeException("There exists no Role with the id " + roleId + ".");
        }
        return roleImpl;
    }
}
