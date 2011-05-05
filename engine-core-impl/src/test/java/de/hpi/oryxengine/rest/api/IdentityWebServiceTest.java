package de.hpi.oryxengine.rest.api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.factory.resource.ParticipantFactory;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractRole;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.IdentityBuilderImpl;
import de.hpi.oryxengine.rest.AbstractJsonServerTest;

/**
 * Tests the interaction with our identity web service.
 */
public class IdentityWebServiceTest extends AbstractJsonServerTest {

    private AbstractParticipant jannik = null;
    private AbstractParticipant tobi = null;
    private AbstractRole r1 = null;
    private AbstractRole r2 = null;

    private static final String PARTICIPANT_URL = "/identity/participants";
    private static final String ROLES_URL = "/identity/roles";

    /**
     * Creates 2 participants.
     */
    public void create2Participants() {

        jannik = ParticipantFactory.createJannik();
        tobi = ParticipantFactory.createTobi();
    }

    /**
     * Creates 2 roles.
     */
    public void create2Roles() {

        IdentityBuilder builder = ServiceFactory.getIdentityService().getIdentityBuilder();
        r1 = builder.createRole("test1");
        r2 = builder.createRole("test2");
    }

    @Override
    protected Class<?> getResource() {

        return IdentityWebService.class;
    }

    /**
     * Test get participants. It should get all participants.
     *
     * @throws URISyntaxException the uRI syntax exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetParticipants()
    throws URISyntaxException, IOException {

        create2Participants();

        String json = makeGETRequestReturningJson(PARTICIPANT_URL);
        Assert.assertNotNull(json);

        JavaType typeRef = TypeFactory.collectionType(Set.class, AbstractParticipant.class);
        Set<AbstractParticipant> participants = this.mapper.readValue(json, typeRef);

        Assert.assertNotNull(participants);
        Assert.assertEquals(participants.size(), 2);
        Assert.assertTrue(participants.contains(jannik));
        Assert.assertTrue(participants.contains(tobi));
    }

    /**
     * Test get participants with no participants.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     */
    @Test
    public void testGetParticipantsWithNoParticipants()
    throws URISyntaxException {

        String json = makeGETRequestReturningJson(PARTICIPANT_URL);

        // [] is an empty JSON
        Assert.assertEquals(json, "[]");
    }

    /**
     * Test getall the roles(happy path).
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetRoles()
    throws URISyntaxException, IOException {

        create2Roles();

        String json = makeGETRequestReturningJson(ROLES_URL);

        JavaType typeRef = TypeFactory.collectionType(Set.class, AbstractRole.class);
        Set<AbstractRole> roles = this.mapper.readValue(json, typeRef);

        Assert.assertNotNull(json);
        Assert.assertNotNull(roles);
        Assert.assertEquals(roles.size(), 2);
        Assert.assertTrue(roles.contains(r1));
        Assert.assertTrue(roles.contains(r2));
    }

    /**
     * Test get roles when there actually are no roles.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     */
    @Test
    public void testGetRolesWithNoRoles()
    throws URISyntaxException {

        String json = makeGETRequestReturningJson(ROLES_URL);
        // [] is an empty JSON.
        Assert.assertEquals(json, "[]");
    }

    /**
     * Tests the participant creation via the REST API.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     */
    @Test
    public void testParticipantCreation()
    throws URISyntaxException {

        String participantName = "Participant";
        String requestUrl = PARTICIPANT_URL;

        makeGenericPOSTRequest(requestUrl, participantName, MediaType.APPLICATION_FORM_URLENCODED);

        IdentityService identity = ServiceFactory.getIdentityService();
        Set<AbstractParticipant> actualParticipants = identity.getParticipants();
        Assert.assertEquals(actualParticipants.size(), 1, "There should be one participant.");
        AbstractParticipant createdParticipant = actualParticipants.iterator().next();
        Assert.assertEquals(createdParticipant.getName(), participantName,
            "The newly created participant should have the desired name.");
    }

    /**
     * Tests the participant deletion via the REST API.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     */
    @Test
    public void testParticipantDeletion()
    throws URISyntaxException {

        String participantName = "Participant";

        IdentityServiceImpl identity = (IdentityServiceImpl) ServiceFactory.getIdentityService();

        IdentityBuilder builder = new IdentityBuilderImpl(identity);
        AbstractParticipant participant = builder.createParticipant(participantName);

        Set<AbstractParticipant> actualParticipants = identity.getParticipants();
        Assert.assertEquals(actualParticipants.size(), 1, "Assure that the participant has been created sucessfully.");

        String requestUrl = PARTICIPANT_URL + "?participant-id=" + participant.getID();

        makeDELETERequest(requestUrl);

        // we need to redo the call here, as the getParticipants()-method always returns a new set.
        actualParticipants = identity.getParticipants();
        Assert.assertEquals(actualParticipants.size(), 0, "There should be no participant left.");

    }

    /**
     * Tests the role creation via the REST API.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     */
    @Test
    public void testRoleCreation()
    throws URISyntaxException {

        String roleName = "Role";
        String requestUrl = ROLES_URL;

        makeGenericPOSTRequest(requestUrl, roleName, MediaType.APPLICATION_FORM_URLENCODED);

        IdentityService identity = ServiceFactory.getIdentityService();
        Set<AbstractRole> actualRoles = identity.getRoles();
        Assert.assertEquals(actualRoles.size(), 1, "There should be one role.");
        AbstractRole createdParticipant = actualRoles.iterator().next();
        Assert.assertEquals(createdParticipant.getName(), roleName,
            "The newly created role should have the desired name.");
    }

    /**
     * Test the deletion of a role via the REST API. We do not check, if any assigned participants still exist, etc., as
     * this should be done in the IdentityService/Builder tests.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     */
    @Test
    public void testRoleDeletion()
    throws URISyntaxException {

        String roleName = "Role";

        IdentityServiceImpl identity = (IdentityServiceImpl) ServiceFactory.getIdentityService();

        IdentityBuilder builder = new IdentityBuilderImpl(identity);
        AbstractRole role = builder.createRole(roleName);

        Set<AbstractRole> actualRoles = identity.getRoles();
        Assert.assertEquals(actualRoles.size(), 1, "Assure that the role has been created sucessfully.");

        String requestUrl = ROLES_URL + "?role-id=" + role.getID();

        makeDELETERequest(requestUrl);

        actualRoles = identity.getRoles();
        Assert.assertEquals(actualRoles.size(), 0, "There should be no role left.");

    }
    
    /**
     * Test the assignment of a participant to an existing role.
     * @throws URISyntaxException 
     */
    @Test
    public void testParticipantToRoleAssignment() throws URISyntaxException {
        // Create a role
        String roleName = "role";
        String participantName = "participant";
        
        IdentityServiceImpl identity = (IdentityServiceImpl) ServiceFactory.getIdentityService();

        IdentityBuilder builder = new IdentityBuilderImpl(identity);
        AbstractRole role = builder.createRole(roleName);
        
        AbstractParticipant participant = builder.createParticipant(participantName);
        
        String requestUrl = ROLES_URL + "/" + role.getID() + "/participants";
//        String json = "{participantIDs : []}";
        String json = "[\"" + participant.getID() + "\"]";
        MockHttpResponse response = makePOSTRequestWithJson(requestUrl, json);
        
        Assert.assertEquals(response.getStatus(), HTTP_STATUS_OK.getStatusCode(), "the result should be ok");
        
        Set<AbstractParticipant> assignedParticipants = role.getParticipantsImmutable();
        Assert.assertEquals(assignedParticipants.size(), 1, "the role has now one participant");
        
        AbstractParticipant assignedParticipant = assignedParticipants.iterator().next();
        Assert.assertEquals(assignedParticipant, participant, "it should be the participant we created");
    }
    
    /**
     * Tests that an error code is returned, if the role does not exist.
     * @throws URISyntaxException 
     */
    @Test
    public void testParticipantToNonExistingRoleAssignment() throws URISyntaxException {
        UUID randomRoleID = UUID.randomUUID();
        String participantName = "participant";
        
        IdentityServiceImpl identity = (IdentityServiceImpl) ServiceFactory.getIdentityService();
        IdentityBuilder builder = new IdentityBuilderImpl(identity);
        AbstractParticipant participant = builder.createParticipant(participantName);
        
        String requestUrl = ROLES_URL + "/" + randomRoleID + "/participants";
        String json = "[\"" + participant.getID() + "\"]";
        MockHttpResponse response = makePOSTRequestWithJson(requestUrl, json);
        
        Assert.assertEquals(response.getStatus(), HTTP_STATUS_FAIL.getStatusCode(), "the result should be a 404");
    }

}
