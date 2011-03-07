package de.hpi.oryxengine.builder;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Position;
import de.hpi.oryxengine.resource.Role;

/**
 * Tests the building of {@link Participant}s in the organization structure.
 */
public class BuildingParticipantTest {

    private IdentityService identityService;
    private IdentityBuilder identityBuilder;
    private Participant participant;

    @BeforeMethod
    public void beforeMethod() {

        identityService = new IdentityServiceImpl();
        identityBuilder = identityService.getIdentityBuilder();

        participant = identityBuilder.createParticipant("gerardo.navarro-suarez");
        participant.setName("Gerardo Navarro Suarez");
    }

    @Test
    public void testParticipantCreation() {

        String failureMessage = "The Identity Service should have one Praticipant.";
        Assert.assertTrue(identityService.getParticipants().size() == 1, failureMessage);
        Assert.assertTrue(identityService.getParticipants().contains(participant), failureMessage);
        Assert.assertEquals(participant.getId(), "gerardo.navarro-suarez");
        Assert.assertEquals(participant.getName(), "Gerardo Navarro Suarez");
    }

    @Test
    public void testForDuplicateParticipant() {

        // Try to create a new Participant with the same Id
        Participant participant2 = identityBuilder.createParticipant("gerardo.navarro-suarez");

        String failureMessage = "There should stil be one Participant";
        Assert.assertTrue(identityService.getParticipants().size() == 1, failureMessage);
        failureMessage = "The new created Participant should be the old one.";
        Assert.assertEquals(participant2.getName(), "Gerardo Navarro Suarez", failureMessage);

    }

    @Test
    public void testCreationParticipantPositionRelationship() {

        Position pos1 = identityBuilder.createPosition("1");
        Position pos2 = identityBuilder.createPosition("2");

        identityBuilder.participantOccupiesPosition(participant, pos1)
                       .participantOccupiesPosition(participant, pos2);

        Assert.assertTrue(identityService.getPositions().size() == 2);
        Assert.assertTrue(participant.getMyPositions().size() == 2);
        String failuremessage = "Pos1 and Pos2 should be held by the participant 'gerardo.navarro-suarez'.";
        Assert.assertEquals(pos1.getPositionHolder(), participant, failuremessage);
        Assert.assertEquals(pos2.getPositionHolder(), participant, failuremessage);
    }

    /**
     * An OrganzationUnit should only have unique Positions.
     */
    @Test
    public void testUniquePositionsParticipantRelationship() {

        Position pos1 = identityBuilder.createPosition("1");
        Position pos2 = identityBuilder.createPosition("2");

        identityBuilder.participantOccupiesPosition(participant, pos1)
                        // Try to occupy the same Position again
                       .participantOccupiesPosition(participant, pos1);

        // As before, there should be only two positions offered by that Participant
        String failureMessage = "Identity Service should have 1 Position Element, but it is "
            + identityService.getPositions().size() + " .";
        Assert.assertTrue(participant.getMyPositions().size() == 1, failureMessage);

        identityBuilder.participantOccupiesPosition(participant, pos2)
                        // Trying to occupy it again
                       .participantOccupiesPosition(participant, pos1);

        // Now there should be one more
        Assert.assertTrue(identityService.getPositions().size() == 2);
    }
    
    @Test
    public void testChangePositionParticipantRelationship() {

        Position pos1 = identityBuilder.createPosition("1");

        Participant participant2 = identityBuilder.createParticipant("jannik.streek");

        identityBuilder.participantOccupiesPosition(participant, pos1)
                       // Now change the Position to another Participant
                       .participantOccupiesPosition(participant2, pos1);

        // There still should be one Position in the system
        Assert.assertTrue(identityService.getPositions().size() == 1);
        Assert.assertTrue(identityService.getPositions().contains(pos1));

        String failureMessage = "The Position '1' should belong to the Participant 'bpt2'.";
        Assert.assertEquals(pos1.getPositionHolder(), participant2, failureMessage);

        failureMessage = "The Participant 'jannik.streek' should have only the Position '1'.";
        Assert.assertTrue(participant2.getMyPositions().size() == 1);
        Assert.assertTrue(participant2.getMyPositions().contains(pos1), failureMessage);

        failureMessage = "The Participant 'bpt1' should not have any Position.";
        Assert.assertTrue(participant.getMyPositions().size() == 0, failureMessage);
    }
    
    @Test
    public void testDeleteParticipant() {

        Position pos1 = identityBuilder.createPosition("1");
        Position pos2 = identityBuilder.createPosition("2");

        identityBuilder.participantOccupiesPosition(participant, pos1)
                       .participantOccupiesPosition(participant, pos2);

        identityBuilder.deleteParticipant(participant);

        String failureMessage = "The Participant 'gerardo.navrro-suarez' should be deleted, but it is still there.";
        Assert.assertTrue(identityService.getParticipants().size() == 0, failureMessage);
        Assert.assertFalse(identityService.getParticipants().contains(participant), failureMessage);

        for (Position position : participant.getMyPositions()) {
            failureMessage = "The Position '" + position.getId() + "' should not have an Participant."
                             + "It should be null.";
            Assert.assertNull(position.getPositionHolder());
        }
    }
    
    /**
     * Test that the relationship between Participant and Position is removed properly.
     */
    @Test
    public void testDeletePositionParticipantRelationship() {

        Position pos1 = identityBuilder.createPosition("1");

        identityBuilder.participantOccupiesPosition(participant, pos1);

        identityBuilder.participantDoesNotOccupyPosition(participant, pos1);

        String failureMessage = "The Participant 'gerardo.navarro-suarez' does not offer any position.";
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
