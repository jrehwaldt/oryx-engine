package de.hpi.oryxengine.resource;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.OrganizationUnit;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Position;
import de.hpi.oryxengine.resource.Role;

/**
 * Tests the IdentityService.
 * 
 *  Especially that retrieved sets are read-only.
 */
public class IdentityServiceTest {
    
    /** The identity service. */
    private IdentityService identityService;

    /**
     * Before class.
     */
    @BeforeClass
    public void beforeClass() {
        identityService = new IdentityServiceImpl();
        
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

        Set<Position> positions = identityService.getPositions();
        Assert.assertNotNull(positions);
        positions.add(null);
    }
    
    /**
     * You should only be able to read the sets provided by the IdentityService.
     */
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testProhibitedOperationsOnOrganizationUnitsSet() {
        
        Set<OrganizationUnit> organizationUnits = identityService.getOrganizationUnits();
        Assert.assertNotNull(organizationUnits);
        organizationUnits.add(null);
    }
    
    /**
     * You should only be able to read the sets provided by the IdentityService.
     */
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testProhibitedOperationsOnParticipantsSet() {
        
        Set<Participant> participants = identityService.getParticipants();
        Assert.assertNotNull(participants);
        participants.add(null);
    }
    
    /**
     * You should only be able to read the sets provided by the IdentityService.
     */
    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testProhibitedOperationsOnRolesSet() {
        
        Set<Role> roles = identityService.getRoles();
        Assert.assertNotNull(roles);
        roles.add(null);
    }
}
