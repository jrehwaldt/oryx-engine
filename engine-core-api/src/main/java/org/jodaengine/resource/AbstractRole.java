package org.jodaengine.resource;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * A Role is a duty or a set of duties that are performed by one or more participants. Imagine that it is a group. For
 * example, a Role could be team XY (representing all participants belonging to that group), the group of all account
 * manager.
 * 
 * A Role could have a superior Role.
 * 
 * @author Gerardo Navarro Suarez
 */
public abstract class AbstractRole extends AbstractResource<AbstractRole> {

    /**
     * Hidden constructor.
     */
    protected AbstractRole() {

    }

    /**
     * The Default Constructor. Creates an {@link AbstractRole} with the given id.
     * 
     * @param resourceID
     *            - the resource id
     * @param positionName
     *            - identifier for the role Object
     */
    public AbstractRole(@Nonnull UUID resourceID, String positionName) {

        super(resourceID, positionName, ResourceType.ROLE);
    }

    /**
     * The Default Constructor. Creates an {@link AbstractRole} with the given id.
     * 
     * @param positionName
     *            - identifier for the position Object
     */
    public AbstractRole(String positionName) {

        super(positionName, ResourceType.ROLE);
    }

    /**
     * Returns the superior Role of the current Role.
     * 
     * @return the superior Role of the current Role
     */
    @JsonProperty
    public abstract AbstractRole getSuperRole();

    /**
     * Returns a read-only Set of all participants belonging to that Role.
     * 
     * @return a read-only Set of all participants belonging to that Role
     */
    @JsonProperty
    @JsonBackReference
    public abstract Set<AbstractParticipant> getParticipantsImmutable();
}
