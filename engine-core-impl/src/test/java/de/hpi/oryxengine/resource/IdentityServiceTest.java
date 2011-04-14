package de.hpi.oryxengine.resource;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.AbstractTest;
import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;

/**
 * Tests the IdentityService.
 * 
 *  Especially that retrieved sets are read-only.
 */
public class IdentityServiceTest extends AbstractTest {
    
    /** The identity service. */
    private IdentityService identityService = null;

    /**
     * Before class.
     */
    @BeforeClass
    public void beforeClass() {
        identityService = ServiceFactory.getIdentityService();
        
        IdentityBuilder identityBuilder = identityService.getIdentityBuilder();
        
        identityBuilder.createOrganizationUnit("bpt");
        identityBuilder.createParticipant("gerardo.navarro-suarez");
        identityBuilder.createPosition("oryx-engine-chef");
        identityBuilder.createRole("admin");
        
    }

    /**
     * You should only be able to read the sets provided by the IdentityService.
     */
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testProhibitedOperationsOnPositionsSet() {

        Set<AbstractPosition> positions = identityService.getPositions();
        Assert.assertNotNull(positions);
        positions.add(null);
    }
    
    /**
     * You should only be able to read the sets provided by the IdentityService.
     */
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testProhibitedOperationsOnOrganizationUnitsSet() {
        
        Set<AbstractOrganizationUnit> organizationUnits = identityService.getOrganizationUnits();
        Assert.assertNotNull(organizationUnits);
        organizationUnits.add(null);
    }
    
    /**
     * You should only be able to read the sets provided by the IdentityService.
     */
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testProhibitedOperationsOnParticipantsSet() {
        
        Set<AbstractParticipant> participants = identityService.getParticipants();
        Assert.assertNotNull(participants);
        participants.add(null);
    }
    
    /**
     * You should only be able to read the sets provided by the IdentityService.
     */
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testProhibitedOperationsOnRolesSet() {
        
        Set<AbstractRole> roles = identityService.getRoles();
        Assert.assertNotNull(roles);
        roles.add(null);
    }
}
