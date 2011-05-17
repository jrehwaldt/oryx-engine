package org.jodaengine.resource;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.jodaengine.IdentityService;
import org.jodaengine.ServiceFactory;
import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.util.testing.AbstractJodaEngineTest;


/**
 * Tests the building of {@link Position}s in the organization structure.
 */
public class BuildingPositionTest extends AbstractJodaEngineTest {

    private IdentityService identityService = null;
    
    private IdentityBuilder identityBuilder = null;
    
    private AbstractPosition position = null;

    /**
     * Set up.
     */
    @BeforeMethod
    public void beforeMethod() {

        identityService = ServiceFactory.getIdentityService();
        identityBuilder = identityService.getIdentityBuilder();
        position = identityBuilder.createPosition("joda-engine-chef");
    }
    
    @Test
    public void testPositionCreation() throws Exception {

        AbstractPosition superior = identityBuilder.createPosition("joda-engine-ober-chef");
        identityBuilder.positionReportsToSuperior(position.getID(), superior.getID());
        
        String failureMessage = "The Identity Service should have two Position.";
        Assert.assertTrue(identityService.getPositions().size() == 2, failureMessage);
        Assert.assertTrue(identityService.getPositions().contains(position), failureMessage);
        Assert.assertTrue(identityService.getPositions().contains(superior), failureMessage);
        Assert.assertEquals(position.getName(), "joda-engine-chef");
        Assert.assertEquals(position.getSuperiorPosition(), superior);
    }
    
    @Test
    public void testForUniquePosition() {
        
        // Try to create a new position with the same Name
        AbstractPosition position2 = identityBuilder.createPosition("joda-engine-chef");
        
        String failureMessage = "There should stil be one Position";
        Assert.assertTrue(identityService.getPositions().size() == 2, failureMessage);
        failureMessage = "The new created Position should not be the old one.";
        Assert.assertNotSame(position2, position, failureMessage);
    }
    
    /**
     * Test not being superior of yourself.
     *
     * @throws Exception the exception
     */
    @Test(expectedExceptions = JodaEngineException.class)
    public void testNotBeingSuperiorOfYourself() throws Exception {

        identityBuilder.positionReportsToSuperior(position.getID(), position.getID());
    }

    /**
     * Test the deletion of a position.
     * 
     * @throws JodaEngineException test fails
     */
    @Test
    public void testDeletePosition() throws JodaEngineException {
        
        AbstractPosition position2 = identityBuilder.createPosition("joda-engine-chef-2");
        AbstractPosition superior = identityBuilder.createPosition("joda-engine-ober-chef");
        
        identityBuilder.positionReportsToSuperior(position.getID(), superior.getID())
                       .positionReportsToSuperior(position2.getID(), superior.getID());
        
        identityBuilder.deletePosition(superior.getID());
        
        String failureMessage = "The Position 'joda-engine-ober-chef' should be deleted, but it is still there.";
        Assert.assertTrue(identityService.getPositions().size() == 2, failureMessage);
        if (identityService.getPositions().contains(superior)) {
            Assert.fail(failureMessage);
        }
        
        failureMessage = "The subordinated Positions should not have any Organization.";
        Assert.assertNull(position.getSuperiorPosition(), failureMessage);
        Assert.assertNull(position2.getSuperiorPosition(), failureMessage);
    }
}
