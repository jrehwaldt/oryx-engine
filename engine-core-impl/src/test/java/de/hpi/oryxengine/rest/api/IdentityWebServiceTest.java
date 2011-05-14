package de.hpi.oryxengine.rest.api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.factory.resource.ParticipantFactory;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractRole;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.util.testing.AbstractJsonServerTest;

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

    private IdentityService identity = null;
    private IdentityBuilder builder = null;

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

        r1 = builder.createRole("test1");
        r2 = builder.createRole("test2");
    }

    @Override
    protected Object getResourceSingleton() {

        return new IdentityWebService(jodaEngineServices);
    }

    /**
     * Sets the identity service and builder that is used in most of the tests.
     */
    @BeforeMethod
    public void setUp() {

        identity = jodaEngineServices.getIdentityService();
        builder = identity.getIdentityBuilder();
    }

    /**
     * Test get participants. It should get all participants.
     * 
     * @throws URISyntaxException
     *             the uri syntax exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
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
     *             the uri syntax exception
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
     *             the uri syntax exception
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
     *             the uri syntax exception
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
     *             the uri syntax exception
     */
    @Test
    public void testParticipantCreation()
    throws URISyntaxException {

        String participantName = "Participant";
        String requestUrl = PARTICIPANT_URL;

        makePOSTRequest(requestUrl, participantName, MediaType.APPLICATION_FORM_URLENCODED);

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
     *             the uri syntax exception
     */
    @Test
    public void testParticipantDeletion()
    throws URISyntaxException {

        String participantName = "Participant";

        AbstractParticipant participant = builder.createParticipant(participantName);

        Set<AbstractParticipant> actualParticipants = identity.getParticipants();
        Assert.assertEquals(actualParticipants.size(), 1, "Assure that the participant has been created sucessfully.");

        String requestUrl = PARTICIPANT_URL + "/" + participant.getID();

        makeDELETERequest(requestUrl);

        // we need to redo the call here, as the getParticipants()-method always returns a new set.
        actualParticipants = identity.getParticipants();
        Assert.assertEquals(actualParticipants.size(), 0, "There should be no participant left.");

    }

    /**
     * Tests the role creation via the REST API.
     * 
     * @throws URISyntaxException
     *             the uri syntax exception
     */
    @Test
    public void testRoleCreation()
    throws URISyntaxException {

        String roleName = "Role";
        String requestUrl = ROLES_URL;

        makePOSTRequest(requestUrl, roleName, MediaType.APPLICATION_FORM_URLENCODED);

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
     *             the uri syntax exception
     */
    @Test
    public void testRoleDeletion()
    throws URISyntaxException {

        String roleName = "Role";

        AbstractRole role = builder.createRole(roleName);

        Set<AbstractRole> actualRoles = identity.getRoles();
        Assert.assertEquals(actualRoles.size(), 1, "Assure that the role has been created sucessfully.");

        String requestUrl = ROLES_URL + "/" + role.getID();

        makeDELETERequest(requestUrl);

        actualRoles = identity.getRoles();
        Assert.assertEquals(actualRoles.size(), 0, "There should be no role left.");

    }

    /**
     * Test the API function to get all participants of a specific role.
     * 
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     * @throws URISyntaxException
     *             the uri syntax exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetParticipantsForRole()
    throws ResourceNotAvailableException, URISyntaxException, IOException {

        String roleName = "Role";
        String participantName = "Participant";

        AbstractParticipant participant = builder.createParticipant(participantName);
        AbstractRole role = builder.createRole(roleName);

        builder.participantBelongsToRole(participant.getID(), role.getID());

        String requestUrl = ROLES_URL + "/" + role.getID() + "/participants";
        MockHttpResponse response = makeGETRequest(requestUrl);
        String json = response.getContentAsString();

        JavaType typeRef = TypeFactory.collectionType(Set.class, AbstractParticipant.class);
        Set<AbstractParticipant> result = this.mapper.readValue(json, typeRef);

        Set<AbstractParticipant> expectedResult = new HashSet<AbstractParticipant>();
        expectedResult.add(participant);

        Assert.assertEquals(result, expectedResult, "there should be one participant in the result");
    }

    /**
     * Test that checks, if a 404 is thrown, if the requested role does not exist.
     * 
     * @throws URISyntaxException
     *             the uri syntax exception
     */
    @Test
    public void testGetParticipantsForNonExistingRole()
    throws URISyntaxException {

        String requestUrl = ROLES_URL + "/" + UUID.randomUUID() + "/participants";
        MockHttpResponse response = makeGETRequest(requestUrl);
        Assert.assertEquals(response.getStatus(), HTTP_STATUS_FAIL.getStatusCode(),
            "If the role does not exists, a 404 should be returned");
    }

    /**
     * Test the assignment of a participant to an existing role.
     * 
     * @throws URISyntaxException
     *             the uri syntax exception
     */
    @Test
    public void testParticipantToRoleAssignment()
    throws URISyntaxException {

        // Create a role
        String roleName = "role";
        String participantName = "participant";

        AbstractRole role = builder.createRole(roleName);

        AbstractParticipant participant = builder.createParticipant(participantName);

        String requestUrl = ROLES_URL + "/" + role.getID() + "/participants";
        // String json = "{participantIDs : []}";
        String json = "{ \"additions\": [\"" + participant.getID() + "\"],"
            + "\"removals\": [], \"@classifier\": \"de.hpi.oryxengine.rest.PatchCollectionChangeset\"}";
        MockHttpResponse response = makePATCHRequest(requestUrl, json, MediaType.APPLICATION_JSON);

        Assert.assertFalse(response.isErrorSent(), "the result should be ok");

        Set<AbstractParticipant> assignedParticipants = role.getParticipantsImmutable();
        Assert.assertEquals(assignedParticipants.size(), 1, "the role has now one participant");

        AbstractParticipant assignedParticipant = assignedParticipants.iterator().next();
        Assert.assertEquals(assignedParticipant, participant, "it should be the participant we created");
    }

    /**
     * Tests that an error code is returned, if additions and removals (participants) are not disjoint.
     * 
     * @throws URISyntaxException
     *             the uri syntax exception
     */
    @Test
    public void testIllegalParticipantToRoleAssignment()
    throws URISyntaxException {

        UUID randomRoleID = UUID.randomUUID();
        String participantName = "participant";

        AbstractParticipant participant = builder.createParticipant(participantName);

        String requestUrl = ROLES_URL + "/" + randomRoleID + "/participants";
        String json = "{ \"additions\": [\"" + participant.getID() + "\"], "
            + "\"removals\": [\"" + participant.getID() + "\"], "
            + "\"@classifier\": \"de.hpi.oryxengine.rest.PatchCollectionChangeset\"}";
        MockHttpResponse response = makePATCHRequest(requestUrl, json, MediaType.APPLICATION_JSON);
        
        Assert.assertEquals(response.getStatus(), HTTP_BAD_REQUEST.getStatusCode(),
            "the result should be a bad-request");
    }

    /**
     * Tests that an error code is returned, if the role does not exist.
     * 
     * @throws URISyntaxException
     *             the uri syntax exception
     */
    @Test
    public void testParticipantToNonExistingRoleAssignment()
    throws URISyntaxException {

        UUID randomRoleID = UUID.randomUUID();
        String participantName = "participant";

        AbstractParticipant participant = builder.createParticipant(participantName);

        String requestUrl = ROLES_URL + "/" + randomRoleID + "/participants";
        String json = "{ \"additions\": [\"" + participant.getID() + "\"],"
            + "\"removals\": [], \"@classifier\": \"de.hpi.oryxengine.rest.PatchCollectionChangeset\"}";
        MockHttpResponse response = makePATCHRequest(requestUrl, json, MediaType.APPLICATION_JSON);

        Assert.assertEquals(response.getStatus(), HTTP_STATUS_FAIL.getStatusCode(), "the result should be a 404");
    }

    /**
     * Test the removal of participants from a role.
     * 
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     * @throws URISyntaxException test fails
     */
    @Test
    public void testRemovalOfParticipantFromRole()
    throws ResourceNotAvailableException, URISyntaxException {

        // Create a role with an assigned participant
        String roleName = "Role";
        String participantName = "Participant";

        AbstractRole role = builder.createRole(roleName);
        AbstractParticipant participant = builder.createParticipant(participantName);

        builder.participantBelongsToRole(participant.getID(), role.getID());

        String requestUrl = ROLES_URL + "/" + role.getID() + "/participants";
        String json = "{ \"additions\": []," + "\"removals\": [\"" + participant.getID()
            + "\"], \"@classifier\": \"de.hpi.oryxengine.rest.PatchCollectionChangeset\"}";

        MockHttpResponse response = makePATCHRequest(requestUrl, json, MediaType.APPLICATION_JSON);
        Assert.assertFalse(response.isErrorSent(), "the result should be ok");

        Set<AbstractParticipant> assignedParticipants = role.getParticipantsImmutable();
        Assert.assertEquals(assignedParticipants.size(), 0, "the role has now one participant");
    }

    /**
     * Tests concurrent operations on the identity service.
     * Focus is on operations on a <i>sole</i> resource.
     * 
     * @throws ResourceNotAvailableException test fails
     * @throws URISyntaxException test fails
     */
    @Test
    public void testConcurrentRemovalAndAddition()
    throws ResourceNotAvailableException, URISyntaxException {

        // Create a role with an assigned participant
        String roleName = "Role";
        String participantName = "Participant";

        AbstractRole role = builder.createRole(roleName);
        AbstractParticipant participant = builder.createParticipant(participantName);

        builder.participantBelongsToRole(participant.getID(), role.getID());

        String requestUrl = ROLES_URL + "/" + role.getID() + "/participants";
        // we try to add AND remove the participant from the role
        String json = "{ \"additions\": [\"" + participant.getID() + "\"]," + "\"removals\": [\"" + participant.getID()
            + "\"], \"@classifier\": \"de.hpi.oryxengine.rest.PatchCollectionChangeset\"}";

        MockHttpResponse response = makePATCHRequest(requestUrl, json, MediaType.APPLICATION_JSON);
        Assert.assertEquals(response.getStatus(), HTTP_BAD_REQUEST.getStatusCode(),
            "the request should not have been processed");

        Set<AbstractParticipant> assignedParticipants = role.getParticipantsImmutable();
        List<AbstractParticipant> expectedAssignedParticipants = new ArrayList<AbstractParticipant>();
        expectedAssignedParticipants.add(participant);
        Assert.assertEquals(assignedParticipants, expectedAssignedParticipants,
            "the role assignement should not have changed");
    }

    /**
     * Create a participant belonging to a specific role.
     *
     * @throws URISyntaxException the uRI syntax exception
     * @throws IOException test fails
     */
    @Test
    public void testCreateParticipantWithRole()
    throws URISyntaxException, IOException {

        String roleName = "test";
        String participantName = "Tobni";

        AbstractRole role = builder.createRole(roleName);

        String requestUrl = ROLES_URL + "/" + role.getID() + "/participants";

        MockHttpResponse response = makePOSTRequest(requestUrl, participantName, MediaType.APPLICATION_FORM_URLENCODED);

        Assert.assertEquals(identity.getParticipants().size(), 1, "No Participant created");
        Assert.assertFalse(role.getParticipantsImmutable().isEmpty(), "There should be a participant of that role");

        AbstractParticipant participant = role.getParticipantsImmutable().iterator().next();
        
        AbstractParticipant part = this.mapper.readValue(response.getContentAsString(), AbstractParticipant.class);
        
        Assert.assertEquals(participant.getName(), participantName,
            "The participant of the role should have the same name as the one we created.");
        Assert.assertEquals(part.getID(), participant.getID());

    }

}
