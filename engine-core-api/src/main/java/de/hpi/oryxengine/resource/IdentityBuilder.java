package de.hpi.oryxengine.resource;

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
     * Creates a new Participant, no matter if there exists already a {@link Participant} with the same name.
     * 
     * @param participantName
     *            - is the name of the instantiated object
     * @return the created Participant object; the object already contains an id
     */
    Participant createParticipant(String participantName);

    /**
     * Deletes the given Participant, so that it is removed from the organization structure.
     * 
     * @param participant
     *            object that should be deleted.
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws Exception
     *             the exception
     */
    IdentityBuilder deleteParticipant(Participant participant)
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
     *            - {@link Participant} that occupies the {@link Position}
     * @param position
     *            - {@link Position} that is occupied by the {@link Participant}
     * @return the current IdentityBuilder in order to continue building the organization structure
     */
    IdentityBuilder participantOccupiesPosition(Participant participant, Position position);

    /**
     * Removes the relationship between a certain Participant and a certain Position.
     * 
     * The given objects are not removed, only the relationship between them is removed.
     * 
     * @param participant
     *            - {@link Participant} that occupies the {@link Position}
     * @param position
     *            - {@link Position} that is occupied by the {@link Participant}
     * @return the current IdentityBuilder in order to continue building the organization structure
     */
    IdentityBuilder participantDoesNotOccupyPosition(Participant participant, Position position);

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
     *            - {@link Participant} that should belong to a {@link Role}
     * @param role
     *            - {@link Role} that contains the {@link Participant}
     * @return the current IdentityBuilder in order to continue building the organization structure
     */
    IdentityBuilder participantBelongsToRole(Participant participant, Role role);

    /**
     * Removes the relationship between a certain Participant and a certain Role.
     * 
     * The given objects are not removed, only the relationship between them is removed.
     * 
     * @param participant
     *            - {@link Participant} that belongs to the {@link Role}
     * @param role
     *            - {@link Role} that contains the {@link Participant}
     * @return the current IdentityBuilder in order to continue building the organization structure
     */
    IdentityBuilder participantDoesNotBelongToRole(Participant participant, Role role);

    /**
     * Participant has capability.
     * 
     * @param participant
     *            the participant
     * @param capability
     *            the capability
     * @return the identity builder
     */
    IdentityBuilder participantHasCapability(Participant participant, Capability capability);

    /**
     * Creates a new OrganizationUnit, no matter if there already exists an {@link OrganizationUnit} with the same name.
     * 
     * @param organizationUnitName
     *            - is the name of the instantiated object
     * @return the created OrganizationUnit object; the object already contains an id
     */
    OrganizationUnit createOrganizationUnit(String organizationUnitName);

    /**
     * Deletes the given OrganizationUnit, so that it is removed from the organization structure.
     * 
     * @param organizationUnit
     *            - object that should be deleted.
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws Exception
     *             the exception
     */
    IdentityBuilder deleteOrganizationUnit(OrganizationUnit organizationUnit)
    throws Exception;

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
    IdentityBuilder organizationUnitOffersPosition(OrganizationUnit organizationUnit, Position position);

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
    IdentityBuilder organizationUnitDoesNotOfferPosition(OrganizationUnit organizationUnit, Position position);

    /**
     * Defines the relationship between two OrganizationUnits.
     * 
     * Calling this method requires that the two OrganizationUnits already exist in the organization structure.
     * 
     * After the method is executed the second OrganizationUnit parameter is the superior OrganizationUnit of the first
     * OrganizationUnit parameter.
     * 
     * @param subOrganizationUnit
     *            - {@link OrganizationUnit} below the superOrganizationUnit
     * @param superOrganizationUnit
     *            - {@link OrganizationUnit} above the superOrganizationUnit
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws DalmatinaException
     *             - in case the sub and super OrganizationUnit are the same
     */
    IdentityBuilder subOrganizationUnitOf(OrganizationUnit subOrganizationUnit, OrganizationUnit superOrganizationUnit)
    throws DalmatinaException;

    /**
     * Creates a new Position, no matter if there exists already a {@link Position} with the same name.
     * 
     * @param positionName
     *            - is the name of the instantiated object
     * @return the created Position object; the object already contains an id.
     */
    Position createPosition(String positionName);

    /**
     * Deletes the given Position, so that it is removed from the organization structure.
     * 
     * @param position
     *            - object that should be deleted.
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws Exception
     *             the exception
     */
    IdentityBuilder deletePosition(Position position)
    throws Exception;

    /**
     * Defines the relationship between two Positions that one position is the superior position of the other position.
     * 
     * Calling this method requires that the two Positions already exist in the organization structure.
     * 
     * After the method is executed the second Position parameter is the superior Position of the first OrganizationUnit
     * parameter.
     * 
     * @param position
     *            - {@link Position} that is below the superior position
     * @param superiorPosition
     *            - {@link Position} that is the superior position of the other one
     * @return the current IdentityBuilder in order to continue building the organization structures
     * @throws DalmatinaException
     *             - in case the position and its superior position are the same
     */
    IdentityBuilder positionReportsToSuperior(Position position, Position superiorPosition)
    throws DalmatinaException;

    /**
     * Creates a new Role, no matter if there exists already a {@link Role} with the same name.
     * 
     * @param roleName
     *            - is the id of the instantiated object
     * @return the created Role object; the object already contains an id
     */
    Role createRole(String roleName);

    /**
     * Deletes the given Role, so that it is removed from the organization structure.
     * 
     * @param role
     *            - object that should be deleted.
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws Exception
     *             the exception
     */
    IdentityBuilder deleteRole(Role role)
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
    IdentityBuilder subRoleOf(Role subRole, Role superRole);

    // Muss noch geschaut werden ob das wirklich schon gebraucht wird
    /**
     * Creates a new Capability.
     * 
     * @param capabilityId
     *            the capability id
     * @return the capability
     */
    Capability createCapability(String capabilityId);
}
