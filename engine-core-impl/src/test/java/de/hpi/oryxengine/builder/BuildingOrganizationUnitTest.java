package de.hpi.oryxengine.builder;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.exception.OryxEngineException;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.OrganizationUnit;
import de.hpi.oryxengine.resource.Position;

/**
 * Tests the building of {@link OrganizationUnit}s in the organization structure.
 */
public class BuildingOrganizationUnitTest {

    private IdentityService identityService;
    private IdentityBuilder identityBuilder;
    private OrganizationUnit organizationUnit;

    @BeforeMethod
    public void beforeMethod() {

        identityService = new IdentityServiceImpl();
        identityBuilder = identityService.getIdentityBuilder();
        organizationUnit = identityBuilder.createOrganizationUnit("bpt");
        organizationUnit.setName("BPT");
    }

    @Test
    public void testOrganizationUnitCreation() {

        OrganizationUnit superOrganizationUnit = identityBuilder.createOrganizationUnit("hpi");

        identityBuilder.subOrganizationUnitOf(organizationUnit, superOrganizationUnit);

        String failuremessage = "There should be two OrganizationUnit.";
        Assert.assertTrue(identityService.getOrganizationUnits().size() == 2, failuremessage);
        failuremessage = "The Organization Unit (Id='bpt', name='BPT') is not in the IdentityService.";
        Assert.assertTrue(identityService.getOrganizationUnits().contains(organizationUnit), failuremessage);
        Assert.assertEquals(organizationUnit.getSuperOrganizationUnit(), superOrganizationUnit);
    }

    @Test
    public void testForDuplicateOrganizationUnit() {

        // Try to create a new OrganizationUnit with the same Id
        OrganizationUnit bpt2 = identityBuilder.createOrganizationUnit("bpt");

        String failureMessage = "There should still be one OrganizationUnit, but there are "
            + identityService.getOrganizationUnits().size();
        Assert.assertTrue(identityService.getOrganizationUnits().size() == 1, failureMessage);
        failureMessage = "The new created OrganizationUnit should be the old one.";
        Assert.assertEquals(bpt2.getName(), "BPT", failureMessage);
    }

    @Test
    public void testRelationshipOrganizationUnitPosition() {

        Position pos1 = identityBuilder.createPosition("1");
        Position pos2 = identityBuilder.createPosition("2");

        identityBuilder.organizationUnitOffersPosition(organizationUnit, pos1)
                       .organizationUnitOffersPosition(organizationUnit, pos2);

        Assert.assertTrue(identityService.getPositions().size() == 2);
        Assert.assertTrue(organizationUnit.getPositions().size() == 2);
        String failuremessage = "Pos1 should belong to the organization 'bpt'.";
        Assert.assertEquals(pos1.belongstoOrganization().getId(), "bpt", failuremessage);
        Assert.assertEquals(pos2.belongstoOrganization().getId(), "bpt", failuremessage);
    }

    /**
     * You should not b able to directly manipulate the relationVariable of the OrganizationUnit.
     */
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testProhibitedOperations() {

        Position pos1 = identityBuilder.createPosition("1");

        organizationUnit.getPositions().add(pos1);
    }

    /**
     * An OrganzationUnit should only have unique Positions.
     */
    @Test
    public void testUniquePositionsInOrganizationUnit() {

        Position pos1 = identityBuilder.createPosition("1");
        Position pos2 = identityBuilder.createPosition("2");

        identityBuilder.organizationUnitOffersPosition(organizationUnit, pos1)
                       // Try to offer the same Position again
                       .organizationUnitOffersPosition(organizationUnit, pos1);

        // As before, there should be only two positions offered by that OrganizationUnit
        String failureMessage = "Identity Service should have 1 Position Element, but it is "
            + organizationUnit.getPositions().size() + " .";
        Assert.assertTrue(organizationUnit.getPositions().size() == 1, failureMessage);

        identityBuilder.organizationUnitOffersPosition(organizationUnit, pos2).organizationUnitOffersPosition(
            organizationUnit, pos1);

        // Now there should be one more
        Assert.assertTrue(organizationUnit.getPositions().size() == 2);
    }

    @Test
    public void testChangePositionInOrganizationUnit() {

        Position pos1 = identityBuilder.createPosition("1");

        OrganizationUnit orgaUnit2 = identityBuilder.createOrganizationUnit("bpt2");

        identityBuilder.organizationUnitOffersPosition(organizationUnit, pos1)
        // Now change the Position to another OrganizationUnit
        .organizationUnitOffersPosition(orgaUnit2, pos1);

        // There still should be one Position in the system
        Assert.assertTrue(identityService.getPositions().size() == 1);
        Assert.assertTrue(identityService.getPositions().contains(pos1));

        String failureMessage = "The Position '1' should belong to the OrganizationUnit 'bpt2'.";
        Assert.assertEquals(pos1.belongstoOrganization(), orgaUnit2, failureMessage);

        failureMessage = "The OrganizationUnit 'bpt2' should have the Position '1'.";
        Assert.assertTrue(orgaUnit2.getPositions().size() == 1);
        Assert.assertTrue(orgaUnit2.getPositions().contains(pos1), failureMessage);

        failureMessage = "The OrganizationUnit 'bpt1' should not have any Position.";
        Assert.assertTrue(organizationUnit.getPositions().size() == 0, failureMessage);
    }

    @Test
    public void testDeleteOrganizationUnit() {

        Position pos1 = identityBuilder.createPosition("1");
        Position pos2 = identityBuilder.createPosition("2");

        identityBuilder.organizationUnitOffersPosition(organizationUnit, pos1).organizationUnitOffersPosition(
            organizationUnit, pos2);

        identityBuilder.deleteOrganizationUnit(organizationUnit);

        String failureMessage = "The OrganizationUnit 'bpt' should be deleted, but it is still there.";
        Assert.assertTrue(identityService.getOrganizationUnits().size() == 0, failureMessage);
        Assert.assertFalse(identityService.getOrganizationUnits().contains(organizationUnit), failureMessage);

        for (Position position : organizationUnit.getPositions()) {
            failureMessage = "The Position '" + position.getId()
                + "' should not have an OrganizationUnit. It should be null.";
            Assert.assertNull(position.belongstoOrganization());
        }
    }

    @Test
    public void testDeleteSuperOrganizationUnit() {

        OrganizationUnit epic = identityBuilder.createOrganizationUnit("epic");
        OrganizationUnit hpi = identityBuilder.createOrganizationUnit("hpi");

        identityBuilder.subOrganizationUnitOf(organizationUnit, hpi).subOrganizationUnitOf(epic, hpi);

        identityBuilder.deleteOrganizationUnit(hpi);

        String failureMessage = "The OrganizationUnit 'hpi' should be deleted, but it is still there.";
        Assert.assertTrue(identityService.getOrganizationUnits().size() == 2);
        Assert.assertFalse(identityService.getOrganizationUnits().contains(hpi), failureMessage);

        failureMessage = "The subordinated OrganizationUnits should not have any Organization.";
        Assert.assertNull(organizationUnit.getSuperOrganizationUnit(), failureMessage);
        Assert.assertNull(epic.getSuperOrganizationUnit(), failureMessage);
    }

    @Test
    public void testDeletePositionInOrganizationUnit() {

        Position pos1 = identityBuilder.createPosition("1");

        identityBuilder.organizationUnitOffersPosition(organizationUnit, pos1);

        identityBuilder.organizationUnitDoesNotOfferPosition(organizationUnit, pos1);

        String failureMessage = "The OrganizationUnit 'bpt' doesnot offer any position.";
        Assert.assertTrue(organizationUnit.getPositions().size() == 0, failureMessage);

    }

    @Test(expectedExceptions = OryxEngineException.class)
    public void testNotBeingSuperOrganizationUnitOfYourself() {

        identityBuilder.subOrganizationUnitOf(organizationUnit, organizationUnit);
    }

}
