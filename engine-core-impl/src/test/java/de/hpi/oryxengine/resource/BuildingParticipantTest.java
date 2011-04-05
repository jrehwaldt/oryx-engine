package de.hpi.oryxengine.resource;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.ServiceFactoryForTesting;

/**
 * Tests the building of {@link Participant}s in the organization structure.
 */
@ContextConfiguration(locations = "/test.oryxengine.cfg.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class BuildingParticipantTest extends AbstractTestNGSpringContextTests {

    private IdentityService identityService = null;

    private IdentityBuilder identityBuilder = null;

    private Participant participant = null;

    @BeforeMethod
    public void beforeMethod() {

        identityService = ServiceFactory.getIdentityService();
        identityBuilder = identityService.getIdentityBuilder();

        participant = identityBuilder.createParticipant("Gerardo Navarro Suarez");
    }

    @AfterMethod
    public void tearDown() {

        ServiceFactoryForTesting.clearIdentityService();
    }

    @Test
    public void testParticipantCreation() {

        String failureMessage = "The Identity Service should have one Praticipant.";
        Assert.assertTrue(identityService.getParticipants().size() == 1, failureMessage);
        Assert.assertTrue(identityService.getParticipants().contains(participant), failureMessage);
        Assert.assertEquals(participant.getName(), "Gerardo Navarro Suarez");
    }

    @Test
    public void testForUniqueParticipant() {

        // Try to create a new Participant with the same Name
        Participant participant2 = identityBuilder.createParticipant("Gerardo Navarro Suarez");

        String failureMessage = "There should be two Participants";
        Assert.assertTrue(identityService.getParticipants().size() == 2, failureMessage);
        failureMessage = "The new created Participant should not be the old one.";
        Assert.assertEquals(participant2.getName(), "Gerardo Navarro Suarez", failureMessage);
        Assert.assertNotSame(participant2, participant);

    }

    @Test
    public void testCreationParticipantPositionRelationship()
    throws Exception {

        Position pos1 = identityBuilder.createPosition("1");
        Position pos2 = identityBuilder.createPosition("2");

        identityBuilder.participantOccupiesPosition(participant.getID(), pos1.getID()).participantOccupiesPosition(participant.getID(), pos2.getID());

        Assert.assertTrue(identityService.getPositions().size() == 2);
        Assert.assertTrue(participant.getMyPositions().size() == 2);
        String failuremessage = "Pos1 and Pos2 should be held by the participant 'Gerardo Navarro Suarez'.";
        Assert.assertEquals(pos1.getPositionHolder(), participant, failuremessage);
        Assert.assertEquals(pos2.getPositionHolder(), participant, failuremessage);
    }

    /**
     * An OrganzationUnit should only have unique Positions.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testUniquePositionsParticipantRelationship()
    throws Exception {

        Position pos1 = identityBuilder.createPosition("1");
        Position pos2 = identityBuilder.createPosition("2");

        identityBuilder.participantOccupiesPosition(participant.getID(), pos1.getID())
        // Try to occupy the same Position again
        .participantOccupiesPosition(participant.getID(), pos1.getID());

        // As before, there should be only two positions offered by that Participant
        String failureMessage = "Identity Service should have 1 Position Element, but it is "
            + identityService.getPositions().size() + " .";
        Assert.assertTrue(participant.getMyPositions().size() == 1, failureMessage);

        identityBuilder.participantOccupiesPosition(participant.getID(), pos2.getID())
        // Trying to occupy it again
        .participantOccupiesPosition(participant.getID(), pos1.getID());

        // Now there should be one more
        Assert.assertTrue(identityService.getPositions().size() == 2);
    }

    @Test
    public void testChangePositionParticipantRelationship()
    throws Exception {

        Position pos1 = identityBuilder.createPosition("1");

        Participant participant2 = identityBuilder.createParticipant("Jannik Streek");

        identityBuilder.participantOccupiesPosition(participant.getID(), pos1.getID())
        // Now change the Position to another Participant
        .participantOccupiesPosition(participant2.getID(), pos1.getID());

        // There still should be one Position in the system
        Assert.assertTrue(identityService.getPositions().size() == 1);
        Assert.assertTrue(identityService.getPositions().contains(pos1));

        String failureMessage = "The Position '1' should belong to the Participant 'Jannik Streek'.";
        Assert.assertEquals(pos1.getPositionHolder(), participant2, failureMessage);

        failureMessage = "The Participant 'Jannik Streek' should have only the Position '1'.";
        Assert.assertTrue(participant2.getMyPositions().size() == 1);
        Assert.assertTrue(participant2.getMyPositions().contains(pos1), failureMessage);

        failureMessage = "The Participant 'Gerardo Navarro Suarez' should not have any Position.";
        Assert.assertTrue(participant.getMyPositions().size() == 0, failureMessage);
    }

    @Test
    public void testDeleteParticipant()
    throws Exception {

        Position pos1 = identityBuilder.createPosition("1");
        Position pos2 = identityBuilder.createPosition("2");

        identityBuilder.participantOccupiesPosition(participant.getID(), pos1.getID()).participantOccupiesPosition(participant.getID(), pos2.getID());

        identityBuilder.deleteParticipant(participant.getID());

        String failureMessage = "The Participant 'Gerardo Navarro Suarez' should be deleted, but it is still there.";
        Assert.assertTrue(identityService.getParticipants().size() == 0, failureMessage);
        Assert.assertFalse(identityService.getParticipants().contains(participant), failureMessage);

        for (Position position : participant.getMyPositions()) {
            failureMessage = "The Position '" + position.getID() + " => " + position.getName()
                + "' should not have an Participant. It should be null.";
            Assert.assertNull(position.getPositionHolder());
        }
    }

    /**
     * Test that the relationship between Participant and Position is removed properly.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testDeletePositionParticipantRelationship()
    throws Exception {

        Position pos1 = identityBuilder.createPosition("1");

        identityBuilder.participantOccupiesPosition(participant.getID(), pos1.getID());

        identityBuilder.participantDoesNotOccupyPosition(participant.getID(), pos1.getID());

        String failureMessage = "The Participant 'Gerardo Navarro Suarez' does not offer any position.";
        Assert.assertTrue(participant.getMyPositions().size() == 0, failureMessage);

    }

    /**
     * You should not be able to directly manipulate the relationVariable of the Participant.
     */
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testProhibitedOperationsOnPositionRelations() {

        Position pos1 = identityBuilder.createPosition("1");

        // Trying to modify the Relations of the Participant directly
        participant.getMyPositions().add(pos1);
    }

    /**
     * You should not be able to directly manipulate the relationVariable of the Participant.
     */
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testProhibitedOperationsOnRoleRelations() {

        Role role = identityBuilder.createRole("admin");

        // Trying to modify the Relations of the Participant directly
        participant.getMyRoles().add(role);
    }
}
