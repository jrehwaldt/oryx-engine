package de.hpi.oryxengine.resource;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.ServiceFactoryForTesting;
import de.hpi.oryxengine.exception.DalmatinaException;

/**
 * Tests the building of {@link OrganizationUnit}s in the organization structure.
 */
public class BuildingOrganizationUnitTest {

    private IdentityService identityService = null;

    private IdentityBuilder identityBuilder = null;

    private OrganizationUnit organizationUnit = null;

    @BeforeMethod
    public void beforeMethod() {

        identityService = ServiceFactory.getIdentityService();
        identityBuilder = identityService.getIdentityBuilder();
        organizationUnit = identityBuilder.createOrganizationUnit("BPT");
    }

    @AfterMethod
    public void tearDown() {

        ServiceFactoryForTesting.clearIdentityService();
    }

    @Test
    public void testOrganizationUnitCreation()
    throws Exception {

        OrganizationUnit superOrganizationUnit = identityBuilder.createOrganizationUnit("HPI");

        identityBuilder.subOrganizationUnitOf(organizationUnit.getID(), superOrganizationUnit.getID());

        String failuremessage = "There should be two OrganizationUnit.";
        Assert.assertTrue(identityService.getOrganizationUnits().size() == 2, failuremessage);
        failuremessage = "The Organization Unit '" + organizationUnit.getID() + " => " + organizationUnit.getName()
            + "' is not in the IdentityService.";
        Assert.assertTrue(identityService.getOrganizationUnits().contains(organizationUnit), failuremessage);
        Assert.assertEquals(organizationUnit.getSuperOrganizationUnit(), superOrganizationUnit);
    }

    @Test
    public void testForUniqueOrganizationUnit() {

        // Try to create a new OrganizationUnit with the same Id
        OrganizationUnit bpt2 = identityBuilder.createOrganizationUnit("BPT");

        String failureMessage = "There should still be two OrganizationUnit, but there are "
            + identityService.getOrganizationUnits().size();
        Assert.assertTrue(identityService.getOrganizationUnits().size() == 2, failureMessage);
        failureMessage = "The new created OrganizationUnit should not be the old one.";
        Assert.assertEquals(bpt2.getName(), "BPT", failureMessage);
        Assert.assertNotSame(bpt2, organizationUnit, failureMessage);
    }

    @Test
    public void testRelationshipOrganizationUnitPosition()
    throws Exception {

        Position pos1 = identityBuilder.createPosition("1");
        Position pos2 = identityBuilder.createPosition("2");

        identityBuilder.organizationUnitOffersPosition(organizationUnit.getID(), pos1.getID())
        .organizationUnitOffersPosition(organizationUnit.getID(), pos2.getID());

        Assert.assertTrue(identityService.getPositions().size() == 2);
        Assert.assertTrue(organizationUnit.getPositions().size() == 2);
        String failuremessage = "Pos1 should belong to the organization 'bpt'.";
        Assert.assertEquals(pos1.belongstoOrganization(), organizationUnit, failuremessage);
        Assert.assertEquals(pos2.belongstoOrganization(), organizationUnit, failuremessage);
    }

    /**
     * You should not be able to directly manipulate the relationVariable of the OrganizationUnit.
     */
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testProhibitedOperations() {

        Position pos1 = identityBuilder.createPosition("1");

        organizationUnit.getPositions().add(pos1);
    }

    /**
     * An OrganzationUnit should only have unique Positions.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void testUniquePositionsInOrganizationUnit()
    throws Exception {

        Position pos1 = identityBuilder.createPosition("1");
        Position pos2 = identityBuilder.createPosition("2");

        identityBuilder.organizationUnitOffersPosition(organizationUnit.getID(), pos1.getID())
        // Try to offer the same Position again
        .organizationUnitOffersPosition(organizationUnit.getID(), pos1.getID());

        // As before, there should be only two positions offered by that OrganizationUnit
        String failureMessage = "Identity Service should have 1 Position Element, but it is "
            + organizationUnit.getPositions().size() + " .";
        Assert.assertTrue(organizationUnit.getPositions().size() == 1, failureMessage);

        identityBuilder.organizationUnitOffersPosition(organizationUnit.getID(), pos2.getID()).organizationUnitOffersPosition(
            organizationUnit.getID(), pos1.getID());

        // Now there should be one more
        Assert.assertTrue(organizationUnit.getPositions().size() == 2);
    }

    @Test
    public void testChangePositionInOrganizationUnit()
    throws Exception {

        Position pos1 = identityBuilder.createPosition("1");

        OrganizationUnit orgaUnit2 = identityBuilder.createOrganizationUnit("HPI");

        identityBuilder.organizationUnitOffersPosition(organizationUnit.getID(), pos1.getID())
        // Now change the Position to another OrganizationUnit
        .organizationUnitOffersPosition(orgaUnit2.getID(), pos1.getID());

        // There still should be one Position in the system
        Assert.assertTrue(identityService.getPositions().size() == 1);
        Assert.assertTrue(identityService.getPositions().contains(pos1));

        String failureMessage = "The Position '1' should belong to the OrganizationUnit 'HPI'.";
        Assert.assertEquals(pos1.belongstoOrganization(), orgaUnit2, failureMessage);

        failureMessage = "The OrganizationUnit 'HPI' should have the Position '1'.";
        Assert.assertTrue(orgaUnit2.getPositions().size() == 1);
        Assert.assertTrue(orgaUnit2.getPositions().contains(pos1), failureMessage);

        failureMessage = "The OrganizationUnit 'BPT' should not have any Position.";
        Assert.assertTrue(organizationUnit.getPositions().size() == 0, failureMessage);
    }

    @Test
    public void testDeleteOrganizationUnit()
    throws Exception {

        Position pos1 = identityBuilder.createPosition("1");
        Position pos2 = identityBuilder.createPosition("2");

        identityBuilder.organizationUnitOffersPosition(organizationUnit.getID(), pos1.getID()).organizationUnitOffersPosition(
            organizationUnit.getID(), pos2.getID());

        identityBuilder.deleteOrganizationUnit(organizationUnit.getID());

        String failureMessage = "The OrganizationUnit 'BPT' should be deleted, but it is still there.";
        Assert.assertTrue(identityService.getOrganizationUnits().size() == 0, failureMessage);
        Assert.assertFalse(identityService.getOrganizationUnits().contains(organizationUnit), failureMessage);

        for (Position position : organizationUnit.getPositions()) {
            failureMessage = "The Position '" + position.getID() + " => " + position.getName()
                + "' should not have an OrganizationUnit. It should be null.";
            Assert.assertNull(position.belongstoOrganization());
        }
    }

    @Test
    public void testDeleteSuperOrganizationUnit()
    throws Exception {

        OrganizationUnit epic = identityBuilder.createOrganizationUnit("EPIC");
        OrganizationUnit hpi = identityBuilder.createOrganizationUnit("HPI");

        identityBuilder.subOrganizationUnitOf(organizationUnit.getID(), hpi.getID()).subOrganizationUnitOf(epic.getID(), hpi.getID());

        identityBuilder.deleteOrganizationUnit(hpi.getID());

        String failureMessage = "The OrganizationUnit 'HPI' should be deleted, but it is still there.";
        Assert.assertTrue(identityService.getOrganizationUnits().size() == 2);
        Assert.assertFalse(identityService.getOrganizationUnits().contains(hpi), failureMessage);

        failureMessage = "The subordinated OrganizationUnits should not have any Organization.";
        Assert.assertNull(organizationUnit.getSuperOrganizationUnit(), failureMessage);
        Assert.assertNull(epic.getSuperOrganizationUnit(), failureMessage);
    }

    @Test
    public void testDeletePositionInOrganizationUnit()
    throws Exception {

        Position pos1 = identityBuilder.createPosition("1");

        identityBuilder.organizationUnitOffersPosition(organizationUnit.getID(), pos1.getID());

        identityBuilder.organizationUnitDoesNotOfferPosition(organizationUnit.getID(), pos1.getID());

        String failureMessage = "The OrganizationUnit 'BPT' doesnot offer any position.";
        Assert.assertTrue(organizationUnit.getPositions().size() == 0, failureMessage);

    }

    @Test(expectedExceptions = DalmatinaException.class)
    public void testNotBeingSuperOrganizationUnitOfYourself()
    throws Exception {

        identityBuilder.subOrganizationUnitOf(organizationUnit.getID(), organizationUnit.getID());
    }

}
