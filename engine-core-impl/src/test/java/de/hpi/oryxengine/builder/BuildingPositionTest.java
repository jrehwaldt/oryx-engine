package de.hpi.oryxengine.builder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.exception.OryxEngineException;
import de.hpi.oryxengine.identity.OrganizationUnit;
import de.hpi.oryxengine.identity.Position;

/**
 * 
 * @author Gerardo Navarro Suarez
 */
public class BuildingPositionTest {

    private IdentityService identityService;
    private IdentityBuilder identityBuilder;

    @BeforeMethod
    public void beforeMethod() {

        identityService = new IdentityServiceImpl();
        identityBuilder = identityService.getIdentityBuilder();
    }

    @AfterMethod
    public void afterMethod() {

    }

    @Test
    public void testPositionCreation() {

        Position position = identityBuilder.createPosition("oryx-engine-chef");
        position.setName("Oryx-Engine-Chef");
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
        
        Position position = identityBuilder.createPosition("oryx-engine-chef");
        position.setName("Gerardo Navarro Suarez");
        
        // Try to create a new position with the same Id
        Position position2 = identityBuilder.createPosition("oryx-engine-chef");
        
        String failureMessage = "There should stil be one Position";
        Assert.assertTrue(identityService.getPositions().size() == 1, failureMessage);
        failureMessage = "The new created Position should be the old one.";
        Assert.assertEquals(position2.getName(), "Gerardo Navarro Suarez", failureMessage);
        
    }
    
    @Test(expectedExceptions = OryxEngineException.class)
    public void testNotBeingSuperiorOfYourself() {

        Position position = identityBuilder.createPosition("oryx-engine-chef");
        identityBuilder.positionReportsToSuperior(position, position);
    }

    @Test
    public void testDeletePosition() {
        
        Position position = identityBuilder.createPosition("oryx-engine-chef");
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
