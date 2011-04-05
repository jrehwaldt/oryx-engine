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
import de.hpi.oryxengine.exception.DalmatinaException;

/**
 * Tests the building of {@link Position}s in the organization structure.
 */
@ContextConfiguration(locations = "/test.oryxengine.cfg.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class BuildingPositionTest extends AbstractTestNGSpringContextTests {

    private IdentityService identityService = null;
    
    private IdentityBuilder identityBuilder = null;
    
    private Position position = null;

    @BeforeMethod
    public void beforeMethod() {

        identityService = ServiceFactory.getIdentityService();
        identityBuilder = identityService.getIdentityBuilder();
        position = identityBuilder.createPosition("Oryx-Engine-Chef");
    }
    
    @AfterMethod
    public void tearDown() {
        
        ServiceFactoryForTesting.clearIdentityService();
    }

    @Test
    public void testPositionCreation() throws Exception {

        Position superior = identityBuilder.createPosition("Oryx-Engine-Ober-Chef");
        identityBuilder.positionReportsToSuperior(position.getID(), superior.getID());
        
        String failureMessage = "The Identity Service should have two Position.";
        Assert.assertTrue(identityService.getPositions().size() == 2, failureMessage);
        Assert.assertTrue(identityService.getPositions().contains(position), failureMessage);
        Assert.assertTrue(identityService.getPositions().contains(superior), failureMessage);
        Assert.assertEquals(position.getName(), "Oryx-Engine-Chef");
        Assert.assertEquals(position.getSuperiorPosition(), superior);
    }
    
    @Test
    public void testForUniquePosition() {
        
        // Try to create a new position with the same Name
        Position position2 = identityBuilder.createPosition("Oryx-Engine-Chef");
        
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
    @Test(expectedExceptions = DalmatinaException.class)
    public void testNotBeingSuperiorOfYourself() throws Exception {

        identityBuilder.positionReportsToSuperior(position.getID(), position.getID());
    }

    @Test
    public void testDeletePosition() throws Exception {
        
        Position position2 = identityBuilder.createPosition("Oryx-Engine-Chef2");
        Position superior = identityBuilder.createPosition("Oryx-Engine-Ober-Chef");        
        
        identityBuilder.positionReportsToSuperior(position.getID(), superior.getID())
                       .positionReportsToSuperior(position2.getID(), superior.getID());

        identityBuilder.deletePosition(superior.getID());
        
        String failureMessage = "The Position 'Oryx-Engine-Ober-Chef' should be deleted, but it is still there.";
        Assert.assertTrue(identityService.getPositions().size() == 2, failureMessage);
        if (identityService.getPositions().contains(superior)) {
            Assert.fail(failureMessage);
        }

        failureMessage = "The subordinated Positions should not have any Organization.";
        Assert.assertNull(position.getSuperiorPosition(), failureMessage);
        Assert.assertNull(position2.getSuperiorPosition(), failureMessage);
    }
}
