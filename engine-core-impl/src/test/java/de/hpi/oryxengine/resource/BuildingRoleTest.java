package de.hpi.oryxengine.resource;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.AbstractTest;
import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;

/**
 * Tests the building of {@link Role}s in the organization structure.
 */
public class BuildingRoleTest extends AbstractTest {

    private IdentityService identityService = null;

    private IdentityBuilder identityBuilder = null;

    private AbstractRole adminRole = null;

    /**
     * Set up.
     */
    @BeforeMethod
    public void beforeMethod() {

        identityService = ServiceFactory.getIdentityService();
        identityBuilder = identityService.getIdentityBuilder();

        adminRole = identityBuilder.createRole("Administrators");
    }

    @Test
    public void testRoleCreation() {

        AbstractRole secretaryRole = identityBuilder.createRole("Secretaries");

        String failuremessage = "There should be two Role.";
        Assert.assertTrue(identityService.getRoles().size() == 2, failuremessage);
        failuremessage = "The Role (Id='admin', name='Administrators') is not in the IdentityService.";
        Assert.assertTrue(identityService.getRoles().contains(adminRole), failuremessage);
        Assert.assertTrue(identityService.getRoles().contains(secretaryRole));

    }

    @Test
    public void testForUniqueRole() {

        // Try to create a new Role with the same Name
        AbstractRole adminRole2 = identityBuilder.createRole("Administrators");

        String failureMessage = "There should still be two Roles, but there are " + identityService.getRoles().size();
        Assert.assertTrue(identityService.getRoles().size() == 2, failureMessage);
        failureMessage = "The new created Role should not be the old one.";
        Assert.assertEquals(adminRole2.getName(), "Administrators", failureMessage);
        Assert.assertNotSame(adminRole, adminRole2);

    }

    @Test
    public void testCreationParticipantRoleRelationship()
    throws Exception {

        AbstractParticipant participant = identityBuilder.createParticipant("Gerardo Navarro Suarez");
        AbstractParticipant participant2 = identityBuilder.createParticipant("Jannik Streek");

        identityBuilder.participantBelongsToRole(participant.getID(), adminRole.getID()).participantBelongsToRole(
            participant2.getID(), adminRole.getID());

        Assert.assertTrue(adminRole.getParticipantsImmutable().size() == 2);
        String failuremessage = "The Participant 'Gerardo Navarro Suarez' should belong to the role 'Administrators'.";
        Assert.assertTrue(participant.getMyRolesImmutable().contains(adminRole), failuremessage);
        Assert.assertTrue(adminRole.getParticipantsImmutable().contains(participant), failuremessage);

        failuremessage = "The Participant 'Jannik Streek' should belong to the role 'Administrators'.";
        Assert.assertTrue(participant.getMyRolesImmutable().contains(adminRole), failuremessage);
        Assert.assertTrue(adminRole.getParticipantsImmutable().contains(participant), failuremessage);
    }

    /**
     * You should not b able to directly manipulate the relationVariable of the Role.
     */
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testProhibitedOperation() {

        AbstractParticipant participant = identityBuilder.createParticipant("Gerardo Navarro Suarez");

        adminRole.getParticipantsImmutable().add(participant);
    }

    @Test
    public void testUniqueParticipantRoleRelationship()
    throws Exception {

        AbstractParticipant participant = identityBuilder.createParticipant("gerardo.navarro-suarez");
        AbstractParticipant participant2 = identityBuilder.createParticipant("jannik.streek");

        identityBuilder.participantBelongsToRole(participant.getID(), adminRole.getID())
        // Try to offer the same Position again
        .participantBelongsToRole(participant.getID(), adminRole.getID());

        // As before, there should be only two positions offered by that Role
        String failureMessage = "Identity Service should have 1 Participant Element, but it is "
            + adminRole.getParticipantsImmutable().size() + " .";
        Assert.assertTrue(adminRole.getParticipantsImmutable().size() == 1, failureMessage);

        identityBuilder.participantBelongsToRole(participant2.getID(), adminRole.getID()).participantBelongsToRole(participant.getID(),
            adminRole.getID());

        // Now there should be one more
        Assert.assertTrue(adminRole.getParticipantsImmutable().size() == 2);
    }

    @Test
    public void testChangeParticipantRoleRelationship()
    throws Exception {

        AbstractParticipant participant = identityBuilder.createParticipant("Gerardo Navarro Suarez");

        AbstractRole secretaryRole = identityBuilder.createRole("Secretaries");

        identityBuilder.participantBelongsToRole(participant.getID(), adminRole.getID())
        // Now change the Position to another Role
        .participantBelongsToRole(participant.getID(), secretaryRole.getID());

        // There still should be one Position in the system
        Assert.assertTrue(identityService.getParticipants().size() == 1);
        Assert.assertTrue(identityService.getParticipants().contains(participant));

        String failureMessage = "The Particapant 'Gerardo Navarro Suarez' should belong to"
            + "the Role 'secretaries' and to the Role 'Aministrators'.";
        Assert.assertTrue(participant.getMyRolesImmutable().contains(adminRole), failureMessage);
        Assert.assertTrue(participant.getMyRolesImmutable().contains(secretaryRole), failureMessage);

        failureMessage = "The Role 'Secretaries' should have only the Participant 'Gerardo Navarro Suarez'.";
        Assert.assertTrue(secretaryRole.getParticipantsImmutable().size() == 1, failureMessage);
        Assert.assertTrue(secretaryRole.getParticipantsImmutable().contains(participant), failureMessage);

        failureMessage = "The Role 'Aministrators' should have only the Participant 'Gerardo Navarro Suarez'.";
        Assert.assertTrue(adminRole.getParticipantsImmutable().size() == 1, failureMessage);
        Assert.assertTrue(adminRole.getParticipantsImmutable().contains(participant), failureMessage);
    }

    @Test
    public void testDeleteRole()
    throws Exception {

        AbstractParticipant participant1 = identityBuilder.createParticipant("Gerardo Navarro Suarez");
        AbstractParticipant participant2 = identityBuilder.createParticipant("Jannik Streek");

        identityBuilder.participantBelongsToRole(
            participant1.getID(), adminRole.getID()).participantBelongsToRole(participant2.getID(), adminRole.getID());

        identityBuilder.deleteRole(adminRole.getID());

        String failureMessage = "The Role 'Administrators' should be deleted, but it is still there.";
        Assert.assertTrue(identityService.getRoles().size() == 0, failureMessage);

        for (AbstractParticipant participant : adminRole.getParticipantsImmutable()) {
            failureMessage = "The Participant '" + participant.getID()
                + "' should not belong to the Role 'Administrators'.";
            Assert.assertFalse(participant.getMyRolesImmutable().contains(adminRole));
        }
    }

    /**
     * Test that the relationship between Participant and Role is removed properly.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testDeleteParticipantRoleRelationship()
    throws Exception {

        AbstractParticipant participant = identityBuilder.createParticipant("Gerardo Navarro Suarez");
        identityBuilder.participantBelongsToRole(participant.getID(), adminRole.getID());

        identityBuilder.participantDoesNotBelongToRole(participant.getID(), adminRole.getID());

        String failureMessage = "The Role 'Administrators' does not contain any participants.";
        Assert.assertTrue(adminRole.getParticipantsImmutable().size() == 0, failureMessage);

    }
}
