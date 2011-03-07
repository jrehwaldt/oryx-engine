package de.hpi.oryxengine.builder;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Role;

/**
 * Tests the building of {@link Role}s in the organization structure.
 */
public class BuildingRoleTest {

    private IdentityService identityService;
    private IdentityBuilder identityBuilder;
    private Role adminRole;

    @BeforeMethod
    public void beforeMethod() {

        identityService = new IdentityServiceImpl();
        identityBuilder = identityService.getIdentityBuilder();

        adminRole = identityBuilder.createRole("admin");
        adminRole.setName("Administrators");
    }

    @Test
    public void testRoleCreation() {

        Role secretaryRole = identityBuilder.createRole("secretaries");

        String failuremessage = "There should be two Role.";
        Assert.assertTrue(identityService.getRoles().size() == 2, failuremessage);
        failuremessage = "The Role (Id='admin', name='Administrators') is not in the IdentityService.";
        Assert.assertTrue(identityService.getRoles().contains(adminRole), failuremessage);
        Assert.assertTrue(identityService.getRoles().contains(secretaryRole));

    }

    @Test
    public void testForDuplicateRole() {

        // Try to create a new Role with the same Id
        Role adminRole2 = identityBuilder.createRole("admin");

        String failureMessage = "There should still be one Role, but there are " + identityService.getRoles().size();
        Assert.assertTrue(identityService.getRoles().size() == 1, failureMessage);
        failureMessage = "The new created Role should be the old one.";
        Assert.assertEquals(adminRole2.getName(), "Administrators", failureMessage);

    }

    @Test
    public void testCreationParticipantRoleRelationship() {

        Participant participant = identityBuilder.createParticipant("gerardo.navarro-suarez");
        Participant participant2 = identityBuilder.createParticipant("jannik.streek");

        identityBuilder.participantBelongsToRole(participant, adminRole).participantBelongsToRole(participant2,
            adminRole);

        Assert.assertTrue(adminRole.getParticipants().size() == 2);
        String failuremessage = "The Participant 'gerardo.navarro-suarez' should belong to the role 'admin'.";
        Assert.assertTrue(participant.getMyRoles().contains(adminRole), failuremessage);
        Assert.assertTrue(adminRole.getParticipants().contains(participant), failuremessage);

        failuremessage = "The Participant 'jannik.streek' should belong to the role 'admin'.";
        Assert.assertTrue(participant.getMyRoles().contains(adminRole), failuremessage);
        Assert.assertTrue(adminRole.getParticipants().contains(participant), failuremessage);
    }

    /**
     * You should not b able to directly manipulate the relationVariable of the Role.
     */
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testProhibitedOperation() {

        Participant participant = identityBuilder.createParticipant("gerardo.navarro-suarez");

        adminRole.getParticipants().add(participant);
    }

    /**
     * An OrganzationUnit should only have unique Positions.
     */
    @Test
    public void testUniqueParticipantRoleRelationship() {

        Participant participant = identityBuilder.createParticipant("gerardo.navarro-suarez");
        Participant participant2 = identityBuilder.createParticipant("jannik.streek");

        identityBuilder.participantBelongsToRole(participant, adminRole)
        // Try to offer the same Position again
        .participantBelongsToRole(participant, adminRole);

        // As before, there should be only two positions offered by that Role
        String failureMessage = "Identity Service should have 1 Participant Element, but it is "
            + adminRole.getParticipants().size() + " .";
        Assert.assertTrue(adminRole.getParticipants().size() == 1, failureMessage);

        identityBuilder.participantBelongsToRole(participant2, adminRole).participantBelongsToRole(participant,
            adminRole);

        // Now there should be one more
        Assert.assertTrue(adminRole.getParticipants().size() == 2);
    }

    @Test
    public void testChangeParticipantRoleRelationship() {

        Participant participant = identityBuilder.createParticipant("gerardo.navarro-suarez");

        Role secretaryRole = identityBuilder.createRole("secretaries");

        identityBuilder.participantBelongsToRole(participant, adminRole)
        // Now change the Position to another Role
        .participantBelongsToRole(participant, secretaryRole);

        // There still should be one Position in the system
        Assert.assertTrue(identityService.getParticipants().size() == 1);
        Assert.assertTrue(identityService.getParticipants().contains(participant));

        String failureMessage = "The Particapant 'gerardo.navarro-suarez' should belong to"
                                + "the Role 'secretaries' and to the Role 'admin'.";
        Assert.assertTrue(participant.getMyRoles().contains(adminRole), failureMessage);
        Assert.assertTrue(participant.getMyRoles().contains(secretaryRole), failureMessage);

        failureMessage = "The Role 'secretaries' should have only the Participant 'gerardo.navarro-suarez'.";
        Assert.assertTrue(secretaryRole.getParticipants().size() == 1, failureMessage);
        Assert.assertTrue(secretaryRole.getParticipants().contains(participant), failureMessage);

        failureMessage = "The Role 'admin' should have only the Participant 'gerardo.navarro-suarez'.";
        Assert.assertTrue(adminRole.getParticipants().size() == 1, failureMessage);
        Assert.assertTrue(adminRole.getParticipants().contains(participant), failureMessage);
    }

    @Test
    public void testDeleteRole() {

        Participant participant1 = identityBuilder.createParticipant("gerardo.navarro-suarez");
        Participant participant2 = identityBuilder.createParticipant("jannik.streek");

        identityBuilder.participantBelongsToRole(participant1, adminRole).participantBelongsToRole(participant2,
            adminRole);

        identityBuilder.deleteRole(adminRole);

        String failureMessage = "The Role 'admin' should be deleted, but it is still there.";
        Assert.assertTrue(identityService.getRoles().size() == 0, failureMessage);

        for (Participant participant : adminRole.getParticipants()) {
            failureMessage = "The Participant '" + participant.getId() + "' should not belong to the Role 'admin'.";
            Assert.assertFalse(participant.getMyRoles().contains(adminRole));
        }
    }

    /**
     * Test that the relationship between Participant and Role is removed properly.
     */
    @Test
    public void testDeleteParticipantRoleRelationship() {

        Participant participant = identityBuilder.createParticipant("gerardo.navarro-suarez");
        identityBuilder.participantBelongsToRole(participant, adminRole);

        identityBuilder.participantDoesNotBelongToRole(participant, adminRole);

        String failureMessage = "The Role 'admin' does not contain any participants.";
        Assert.assertTrue(adminRole.getParticipants().size() == 0, failureMessage);

    }
}
