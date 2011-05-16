package org.jodaengine.resource;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.exception.ResourceNotAvailableException;

/**
 * The ResourceBuilder provides an easy and intuitive way to define and customize the enterprise's organization
 * structures.
 * 
 * You can CRUD participantIDs, OrganizationUnits, ... You can define the relations between them
 * 
 * @author Gerardo Navarro Suarez
 */
public interface IdentityBuilder {

    /**
     * Creates a new participantID, no matter if there exists already a {@link AbstractparticipantID} with the same
     * name.
     * 
     * @param participantName
     *            - is the name of the instantiated object
     * @return the created participantID object; the object already contains an id
     */
    @Nonnull
    AbstractParticipant createParticipant(@Nonnull String participantName);

    /**
     * Deletes the given participantID, so that it is removed from the organization structure.
     * 
     * @param participantID
     *            object that should be deleted.
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws ResourceNotAvailableException
     *             thrown if the participant is missing
     */
    @Nonnull
    IdentityBuilder deleteParticipant(@Nonnull UUID participantID)
    throws ResourceNotAvailableException;

    /**
     * Defines the relationship between a participantID and a positionID.
     * 
     * Be aware that a positionID can only be occupied by only one participantID. But a participantID can occupy several
     * positionIDs.
     * 
     * Calling this method requires that the participantID and the positionID already exist in the organization
     * structure.
     * 
     * @param participantID
     *            - {@link AbstractparticipantID} that occupies the {@link AbstractpositionID}
     * @param positionID
     *            - {@link AbstractpositionID} that is occupied by the {@link AbstractparticipantID}
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    @Nonnull
    IdentityBuilder participantOccupiesPosition(@Nonnull UUID participantID, @Nonnull UUID positionID)
    throws ResourceNotAvailableException;

    /**
     * Removes the relationship between a certain participantID and a certain positionID.
     * 
     * The given objects are not removed, only the relationship between them is removed.
     * 
     * @param participantID
     *            - {@link AbstractparticipantID} that occupies the {@link AbstractpositionID}
     * @param positionID
     *            - {@link AbstractpositionID} that is occupied by the {@link AbstractparticipantID}
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    @Nonnull
    IdentityBuilder participantDoesNotOccupyPosition(@Nonnull UUID participantID, @Nonnull UUID positionID)
    throws ResourceNotAvailableException;

    /**
     * Builds a relationship between a participantID and a roleID which represents that a participantIDs belongs to an
     * roleID.
     * 
     * Calling this method requires that the participantID and the roleID already exist in the organization structure.
     * 
     * Be aware that a participantID Unit could belong to several roleIDs. So calling this method twice with the same
     * participantID parameter does not create two relationships cause the second participantID is already there.
     * 
     * Be also aware that a roleIDs could have several participantIDs, too.
     * 
     * @param participantID
     *            - {@link AbstractparticipantID} that should belong to a {@link AbstractroleID}
     * @param roleID
     *            - {@link AbstractroleID} that contains the {@link AbstractparticipantID}
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    @Nonnull
    IdentityBuilder participantBelongsToRole(@Nonnull UUID participantID, @Nonnull UUID roleID)
    throws ResourceNotAvailableException;

    /**
     * Removes the relationship between a certain participantID and a certain roleID.
     * 
     * The given objects are not removed, only the relationship between them is removed.
     * 
     * @param participantID
     *            - {@link AbstractparticipantID} that belongs to the {@link AbstractroleID}
     * @param roleID
     *            - {@link AbstractroleID} that contains the {@link AbstractparticipantID}
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    @Nonnull
    IdentityBuilder participantDoesNotBelongToRole(@Nonnull UUID participantID, @Nonnull UUID roleID)
    throws ResourceNotAvailableException;

    /**
     * participantID has capability.
     * 
     * @param participantID
     *            the participantID
     * @param capability
     *            the capability
     * @return the identity builder
     */
    @Nonnull
    IdentityBuilder participantHasCapability(@Nonnull UUID participantID, @Nonnull AbstractCapability capability);

    /**
     * Creates a new OrganizationUnit, no matter if there already exists an {@link AbstractOrganizationUnit} with the
     * same name.
     * 
     * @param organizationUnitName
     *            - is the name of the instantiated object
     * @return the created OrganizationUnit object; the object already contains an id
     */
    AbstractOrganizationUnit createOrganizationUnit(@Nonnull String organizationUnitName);

    /**
     * Deletes the given OrganizationUnit, so that it is removed from the organization structure.
     * 
     * @param organizationUnit
     *            - object that should be deleted.
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws JodaEngineException
     *             the exception
     */
    @Nonnull
    IdentityBuilder deleteOrganizationUnit(@Nonnull UUID organizationUnit)
    throws JodaEngineException;

    /**
     * Builds a relationship between an OrganizationUnit and a positionID which represents that a positionID belongs to
     * an OrganizationUnit.
     * 
     * Calling this method requires that the OrganizationUnit and positionID already exist in the organization
     * structure.
     * 
     * Be aware that an OrganizationUnit could have several positionIDs. So calling this method twice with the same
     * positionID parameter does not create two relationships cause the second positionID is already there.
     * 
     * Be also aware that, only one positionID is related to an OrganizationUnit. Using this method assures this
     * constraint.
     * 
     * @param organizationUnit
     *            - part of the relationship
     * @param positionID
     *            - that belongs to the organizationUnit
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    @Nonnull
    IdentityBuilder organizationUnitOffersPosition(@Nonnull UUID organizationUnit, @Nonnull UUID positionID)
    throws ResourceNotAvailableException;

    /**
     * Removes the relationship between the certain OrganizationUnit and the certain positionID.
     * 
     * The given objects are not removed, only the relationship between them is removed.
     * 
     * @param organizationUnit
     *            - part of the relationship
     * @param positionID
     *            - part of the relationship
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    @Nonnull
    IdentityBuilder organizationUnitDoesNotOfferPosition(@Nonnull UUID organizationUnit, @Nonnull UUID positionID)
    throws ResourceNotAvailableException;

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
     * @throws JodaEngineException
     *             - in case the sub and super OrganizationUnit are the same
     */
    @Nonnull
    IdentityBuilder subOrganizationUnitOf(@Nonnull UUID subOrganizationUnit, @Nonnull UUID superOrganizationUnit)
    throws JodaEngineException;

    /**
     * Creates a new positionID, no matter if there exists already a {@link AbstractpositionID} with the same name.
     * 
     * @param positionName
     *            - is the name of the instantiated object
     * @return the created positionID object; the object already contains an id.
     */
    AbstractPosition createPosition(@Nonnull String positionName);

    /**
     * Deletes the given positionID, so that it is removed from the organization structure.
     * 
     * @param positionID
     *            - object that should be deleted.
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws JodaEngineException
     *             the exception
     */
    @Nonnull
    IdentityBuilder deletePosition(@Nonnull UUID positionID)
    throws JodaEngineException;

    /**
     * Defines the relationship between two positionIDs that one positionID is the superior positionID of the other
     * positionID.
     * 
     * Calling this method requires that the two positionIDs already exist in the organization structure.
     * 
     * After the method is executed the second positionID parameter is the superior positionID of the first
     * OrganizationUnit parameter.
     * 
     * @param positionID
     *            - {@link AbstractpositionID} that is below the superior positionID
     * @param superiorpositionID
     *            - {@link AbstractpositionID} that is the superior positionID of the other one
     * @return the current IdentityBuilder in order to continue building the organization structures
     * @throws JodaEngineException
     *             - in case the positionID and its superior positionID are the same
     */
    @Nonnull
    IdentityBuilder positionReportsToSuperior(@Nonnull UUID positionID, @Nonnull UUID superiorpositionID)
    throws JodaEngineException;

    /**
     * Creates a new roleID, no matter if there exists already a {@link AbstractroleID} with the same name.
     * 
     * @param roleName
     *            - is the id of the instantiated object
     * @return the created roleID object; the object already contains an id
     */
    AbstractRole createRole(@Nonnull String roleName);

    /**
     * Deletes the given roleID, so that it is removed from the organization structure.
     * 
     * @param roleID
     *            - object that should be deleted.
     * @return the current IdentityBuilder in order to continue building the organization structure
     * @throws Exception
     *             the exception
     */
    @Nonnull
    IdentityBuilder deleteRole(@Nonnull UUID roleID)
    throws Exception;

    /**
     * Sub roleID of.
     * 
     * @param subRoleID
     *            - the sub roleID
     * @param superRoleID
     *            - the super roleID
     * @return the identity builder
     */
    @Nonnull
    IdentityBuilder subRoleOf(@Nonnull UUID subRoleID, @Nonnull UUID superRoleID);

    // Muss noch geschaut werden ob das wirklich schon gebraucht wird
    /**
     * Creates a new Capability.
     * 
     * @param capabilityId
     *            the capability id
     * @return the capability
     */
    AbstractCapability createCapability(@Nonnull String capabilityId);
}
