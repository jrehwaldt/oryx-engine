package de.hpi.oryxengine.resource;

import java.util.UUID;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.exception.DalmatinaException;

/**
 * The ResourceBuilder provides an easy and intuitive way to define and customize the enterprise's organization
 * structures.
 * 
 * You can CRUD Participants, OrganizationUnits, ... You can define the relations between them
 * 
 * @author Gerardo Navarro Suarez
 */
public interface IdentityBuilder {

    /**
     * Creates a new Participant, no matter if there exists already a {@link AbstractParticipant} with the same name.
     * 
     * @param participantName
     *            - is the name of the instantiated object
     * @return the created Participant object; the object already contains an id
     */
    @Nonnull Participant createParticipant(@Nonnull String participantName);

    /**
     * Deletes the given Participant, so that it is removed from the organization structure.
     * 
     * @param participant
     *            object that should be deleted.
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws Exception
     *             the exception
     */
    @Nonnull IdentityBuilder deleteParticipant(@Nonnull UUID participant)
    throws Exception;

    /**
     * Defines the relationship between a Participant and a Position.
     * 
     * Be aware that a Position can only be occupied by only one Participant. But a Participant can occupy several
     * Positions.
     * 
     * Calling this method requires that the Participant and the Position already exist in the organization structure.
     * 
     * @param participant
     *            - {@link AbstractParticipant} that occupies the {@link AbstractPosition}
     * @param position
     *            - {@link AbstractPosition} that is occupied by the {@link AbstractParticipant}
     * @return the current IdentityBuilder in order to continue building the organization structure
     */
    @Nonnull IdentityBuilder participantOccupiesPosition(@Nonnull UUID participant,
                                                         @Nonnull UUID position);

    /**
     * Removes the relationship between a certain Participant and a certain Position.
     * 
     * The given objects are not removed, only the relationship between them is removed.
     * 
     * @param participant
     *            - {@link AbstractParticipant} that occupies the {@link AbstractPosition}
     * @param position
     *            - {@link AbstractPosition} that is occupied by the {@link AbstractParticipant}
     * @return the current IdentityBuilder in order to continue building the organization structure
     */
    @Nonnull IdentityBuilder participantDoesNotOccupyPosition(@Nonnull UUID participant,
                                                              @Nonnull UUID position);

    /**
     * Builds a relationship between a Participant and a Role which represents that a Participants belongs to an Role.
     * 
     * Calling this method requires that the Participant and the Role already exist in the organization structure.
     * 
     * Be aware that a Participant Unit could belong to several Roles. So calling this method twice with the same
     * Participant parameter does not create two relationships cause the second Participant is already there.
     * 
     * Be also aware that a Roles could have several Participants, too.
     * 
     * @param participant
     *            - {@link AbstractParticipant} that should belong to a {@link AbstractRole}
     * @param role
     *            - {@link AbstractRole} that contains the {@link AbstractParticipant}
     * @return the current IdentityBuilder in order to continue building the organization structure
     */
    @Nonnull IdentityBuilder participantBelongsToRole(@Nonnull UUID participant,
                                                      @Nonnull UUID role);

    /**
     * Removes the relationship between a certain Participant and a certain Role.
     * 
     * The given objects are not removed, only the relationship between them is removed.
     * 
     * @param participant
     *            - {@link AbstractParticipant} that belongs to the {@link AbstractRole}
     * @param role
     *            - {@link AbstractRole} that contains the {@link AbstractParticipant}
     * @return the current IdentityBuilder in order to continue building the organization structure
     */
    @Nonnull IdentityBuilder participantDoesNotBelongToRole(@Nonnull UUID participant,
                                                            @Nonnull UUID role);

    /**
     * Participant has capability.
     * 
     * @param participant
     *            the participant
     * @param capability
     *            the capability
     * @return the identity builder
     */
    @Nonnull IdentityBuilder participantHasCapability(@Nonnull UUID participant,
                                                      @Nonnull Capability capability);

    /**
     * Creates a new OrganizationUnit, no matter if there already exists an {@link AbstractOrganizationUnit}
     * with the same name.
     * 
     * @param organizationUnitName
     *            - is the name of the instantiated object
     * @return the created OrganizationUnit object; the object already contains an id
     */
    OrganizationUnit createOrganizationUnit(@Nonnull String organizationUnitName);

    /**
     * Deletes the given OrganizationUnit, so that it is removed from the organization structure.
     * 
     * @param organizationUnit
     *            - object that should be deleted.
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws DalmatinaException
     *             the exception
     */
    @Nonnull IdentityBuilder deleteOrganizationUnit(@Nonnull UUID organizationUnit)
    throws DalmatinaException;

    /**
     * Builds a relationship between an OrganizationUnit and a Position which represents that a Position belongs to an
     * OrganizationUnit.
     * 
     * Calling this method requires that the OrganizationUnit and Position already exist in the organization structure.
     * 
     * Be aware that an OrganizationUnit could have several Positions. So calling this method twice with the same
     * Position parameter does not create two relationships cause the second Position is already there.
     * 
     * Be also aware that, only one Position is related to an OrganizationUnit. Using this method assures this
     * constraint.
     * 
     * @param organizationUnit
     *            - part of the relationship
     * @param position
     *            - that belongs to the organizationUnit
     * @return the current IdentityBuilder in order to continue building the organization structure
     */
    @Nonnull IdentityBuilder organizationUnitOffersPosition(@Nonnull UUID organizationUnit,
                                                            @Nonnull UUID position);

    /**
     * Removes the relationship between the certain OrganizationUnit and the certain Position.
     * 
     * The given objects are not removed, only the relationship between them is removed.
     * 
     * @param organizationUnit
     *            - part of the relationship
     * @param position
     *            - part of the relationship
     * @return the current IdentityBuilder in order to continue building the organization structure
     */
    @Nonnull IdentityBuilder organizationUnitDoesNotOfferPosition(@Nonnull UUID organizationUnit,
                                                                  @Nonnull UUID position);

    /**
     * Defines the relationship between two OrganizationUnits.
     * 
     * Calling this method requires that the two OrganizationUnits already exist in the organization structure.
     * 
     * After the method is executed the second OrganizationUnit parameter is the superior OrganizationUnit of the first
     * OrganizationUnit parameter.
     * 
     * @param subOrganizationUnit
     *            - {@link AbstractOrganizationUnit} below the superOrganizationUnit
     * @param superOrganizationUnit
     *            - {@link AbstractOrganizationUnit} above the superOrganizationUnit
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws DalmatinaException
     *             - in case the sub and super OrganizationUnit are the same
     */
    @Nonnull IdentityBuilder subOrganizationUnitOf(@Nonnull UUID subOrganizationUnit,
                                                   @Nonnull UUID superOrganizationUnit)
    throws DalmatinaException;

    /**
     * Creates a new Position, no matter if there exists already a {@link AbstractPosition} with the same name.
     * 
     * @param positionName
     *            - is the name of the instantiated object
     * @return the created Position object; the object already contains an id.
     */
    Position createPosition(@Nonnull String positionName);

    /**
     * Deletes the given Position, so that it is removed from the organization structure.
     * 
     * @param position
     *            - object that should be deleted.
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws DalmatinaException
     *             the exception
     */
    @Nonnull IdentityBuilder deletePosition(@Nonnull UUID position)
    throws DalmatinaException;

    /**
     * Defines the relationship between two Positions that one position is the superior position of the other position.
     * 
     * Calling this method requires that the two Positions already exist in the organization structure.
     * 
     * After the method is executed the second Position parameter is the superior Position of the first OrganizationUnit
     * parameter.
     * 
     * @param position
     *            - {@link AbstractPosition} that is below the superior position
     * @param superiorPosition
     *            - {@link AbstractPosition} that is the superior position of the other one
     * @return the current IdentityBuilder in order to continue building the organization structures
     * @throws DalmatinaException
     *             - in case the position and its superior position are the same
     */
    @Nonnull IdentityBuilder positionReportsToSuperior(@Nonnull UUID position,
                                                       @Nonnull UUID superiorPosition)
    throws DalmatinaException;

    /**
     * Creates a new Role, no matter if there exists already a {@link AbstractRole} with the same name.
     * 
     * @param roleName
     *            - is the id of the instantiated object
     * @return the created Role object; the object already contains an id
     */
    Role createRole(@Nonnull String roleName);

    /**
     * Deletes the given Role, so that it is removed from the organization structure.
     * 
     * @param role
     *            - object that should be deleted.
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws Exception
     *             the exception
     */
    @Nonnull IdentityBuilder deleteRole(@Nonnull UUID role)
    throws Exception;

    /**
     * Sub role of.
     * 
     * @param subRole
     *            the sub role
     * @param superRole
     *            the super role
     * @return the identity builder
     */
    @Nonnull IdentityBuilder subRoleOf(@Nonnull UUID subRole,
                                       @Nonnull UUID superRole);

    // Muss noch geschaut werden ob das wirklich schon gebraucht wird
    /**
     * Creates a new Capability.
     * 
     * @param capabilityId
     *            the capability id
     * @return the capability
     */
    Capability createCapability(@Nonnull String capabilityId);
}
