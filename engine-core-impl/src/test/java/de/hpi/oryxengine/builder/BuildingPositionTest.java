package de.hpi.oryxengine.builder;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.exception.OryxEngineException;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.Position;

/**
 * Tests the building of {@link Position}s in the organization structure.
 */
public class BuildingPositionTest {

    private IdentityService identityService;
    private IdentityBuilder identityBuilder;
    private Position position;

    @BeforeMethod
    public void beforeMethod() {

        identityService = new IdentityServiceImpl();
        identityBuilder = identityService.getIdentityBuilder();
        position = identityBuilder.createPosition("oryx-engine-chef");
        position.setName("Oryx-Engine-Chef");
    }

    @Test
    public void testPositionCreation() {

        Position superior = identityBuilder.createPosition("oryx-engine-ober-chef");
        identityBuilder.positionReportsToSuperior(position, superior);
        
        String failureMessage = "The Identity Service should have two Position.";
        Assert.assertTrue(identityService.getPositions().size() == 2, failureMessage);
        Assert.assertTrue(identityService.getPositions().contains(position), failureMessage);
        Assert.assertTrue(identityService.getPositions().contains(superior), failureMessage);
        Assert.assertEquals(position.getId(), "oryx-engine-chef");
        Assert.assertEquals(position.getName(), "Oryx-Engine-Chef");
        Assert.assertEquals(position.getSuperiorPosition(), superior);
    }
    
    @Test
    public void testForDuplicatePosition() {
        
        // Try to create a new position with the same Id
        Position position2 = identityBuilder.createPosition("oryx-engine-chef");
        
        String failureMessage = "There should stil be one Position";
        Assert.assertTrue(identityService.getPositions().size() == 1, failureMessage);
        failureMessage = "The new created Position should be the old one.";
        Assert.assertEquals(position2.getName(), "Gerardo Navarro Suarez", failureMessage);
        
    }
    
    @Test(expectedExceptions = OryxEngineException.class)
    public void testNotBeingSuperiorOfYourself() {

        identityBuilder.positionReportsToSuperior(position, position);
    }

    @Test
    public void testDeletePosition() {
        
        Position position2 = identityBuilder.createPosition("oryx-engine-chef2");
        Position superior = identityBuilder.createPosition("oryx-engine-ober-chef");        
        
        identityBuilder.positionReportsToSuperior(position, superior)
                       .positionReportsToSuperior(position2, superior);

        identityBuilder.deletePosition(superior);
        
        String failureMessage = "The Position 'oryx-engine-ober-chef' should be deleted, but it is still there.";
        Assert.assertTrue(identityService.getPositions().size() == 2, failureMessage);
        if (identityService.getPositions().contains(superior)) {
            Assert.fail(failureMessage);
        }

        failureMessage = "The subordinated Positions should not have any Organization.";
        Assert.assertNull(position.getSuperiorPosition(), failureMessage);
        Assert.assertNull(position2.getSuperiorPosition(), failureMessage);
    }
}
