package org.jodaengine.rest.api;

import static org.mockito.Mockito.mock;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import org.jboss.resteasy.mock.MockHttpResponse;
import org.jodaengine.RepositoryService;
import org.jodaengine.ServiceFactory;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.factory.resource.ParticipantFactory;
import org.jodaengine.factory.worklist.CreationPatternFactory;
import org.jodaengine.process.token.Token;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.AbstractRole;
import org.jodaengine.resource.allocation.CreationPattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.resource.worklist.WorklistItemState;
import org.jodaengine.util.testing.AbstractJsonServerTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The Class WorklistItemClaimingTest.
 */
public class WorklistItemStatusTest extends AbstractJsonServerTest {

    private CreationPattern pattern;
    private AbstractRole assignedRole;
    private AbstractResource<?> participant;
    private AbstractResource<?> badParticipant;

    @Override
    protected Object getResourceSingleton() {

        return new WorklistWebService(jodaEngineServices);
    }

    /**
     * Make a REST call to complete a worklist item.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     */
    @Test
    public void testItemCompleting()
    throws URISyntaxException {

        List<AbstractWorklistItem> participantItems = ServiceFactory.getWorklistService().getWorklistItems(participant);
        AbstractWorklistItem item = participantItems.get(0);

        // Begin the item
        ServiceFactory.getWorklistService().beginWorklistItemBy(item, participant);

        MockHttpResponse response = makePUTRequestWithText("/worklist/items/" + item.getID() + "/state?participantId="
            + participant.getID(), "COMPLETED");

        Assert.assertEquals(response.getStatus(), HTTP_STATUS_OK.getStatusCode(),
            "the result should be OK, that means, the request should have suceeded.");

        Assert.assertEquals(item.getStatus(), WorklistItemState.COMPLETED, "the item should be completed now.");

        // refresh the item list
        participantItems = ServiceFactory.getWorklistService().getWorklistItems(participant);
        Assert.assertEquals(participantItems.size(), 0,
            "the item should not be in the participant's worklist any longer.");

    }

    /**
     * Make a REST call to begin a worklist item.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     */
    @Test
    public void testItemBeginning()
    throws URISyntaxException {

        List<AbstractWorklistItem> participantItems = ServiceFactory.getWorklistService().getWorklistItems(participant);
        AbstractWorklistItem item = participantItems.get(0);

        // Claim the item
        ServiceFactory.getWorklistService().claimWorklistItemBy(item, participant);

        MockHttpResponse response = makePUTRequestWithText("/worklist/items/" + item.getID() + "/state?participantId="
            + participant.getID(), "EXECUTING");

        Assert.assertEquals(response.getStatus(), HTTP_STATUS_OK.getStatusCode(),
            "the result should be OK, that means, the request should have suceeded.");

        Assert.assertEquals(item.getStatus(), WorklistItemState.EXECUTING, "the item should be completed now.");

        List<AbstractWorklistItem> roleItems = ServiceFactory.getWorklistService().getWorklistItems(assignedRole);
        Assert.assertEquals(roleItems.size(), 0, "the item should not be in the role's worklist any longer.");

    }

    /**
     * Make a REST call to claim a worklist item.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     */
    @Test
    public void testItemClaiming()
    throws URISyntaxException {

        List<AbstractWorklistItem> participantItems = ServiceFactory.getWorklistService().getWorklistItems(participant);
        AbstractWorklistItem item = participantItems.get(0);

        MockHttpResponse response = makePUTRequestWithText("/worklist/items/" + item.getID() + "/state?participantId="
            + participant.getID(), "ALLOCATED");

        Assert.assertEquals(response.getStatus(), HTTP_STATUS_OK.getStatusCode(),
            "the result should be OK, that means, the request should have suceeded.");

        Assert.assertEquals(item.getStatus(), WorklistItemState.ALLOCATED, "the item should be allocated now.");

        List<AbstractWorklistItem> roleItems = ServiceFactory.getWorklistService().getWorklistItems(assignedRole);
        Assert.assertEquals(roleItems.size(), 0, "the item should not be in the role's worklist any longer.");

    }

    /**
     * Make a REST concurrent call to claim a worklist item which was already claimed.
     * The bad participant should get a 404 http response, because the item is not longer
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     */
    @Test
    public void testItemConcurrentClaiming()
    throws URISyntaxException {

        List<AbstractWorklistItem> participantItems = ServiceFactory.getWorklistService().getWorklistItems(participant);
        AbstractWorklistItem item = participantItems.get(0);

        MockHttpResponse response = makePUTRequestWithText("/worklist/items/" + item.getID() + "/state?participantId="
            + participant.getID(), "ALLOCATED");

        Assert.assertEquals(response.getStatus(), HTTP_STATUS_OK.getStatusCode(),
            "the result should be OK, that means, the request should have suceeded.");

        response = makePUTRequestWithText(
            "/worklist/items/" + item.getID() + "/state?participantId=" + badParticipant.getID(), "ALLOCATED");

        Assert.assertEquals(response.getStatus(), HTTP_STATUS_FAIL.getStatusCode(),
            "the result should be a status code 404, that means, the request has failed.");

    }

    /**
     * Sets the up task and gets the assigned role and one of the participants that belongs to this role.
     * 
     * @throws ResourceNotAvailableException test fails
     */
    @BeforeMethod
    public void setUpTask()
    throws ResourceNotAvailableException {

        pattern = CreationPatternFactory.createRoleBasedDistribution();
        Token token = mock(Token.class);
//        ServiceFactory.getTaskDistribution().distribute(pattern, token);
        AbstractWorklistItem item = pattern.createWorklistItem(token, mock(RepositoryService.class));
        pattern.getPushPattern().distributeItem(ServiceFactory.getInteralWorklistService(), item);

        // get the participants that are assigned to the role that this task was assigned to.
        assignedRole = (AbstractRole) item.getAssignedResources().iterator().next();
        Iterator<AbstractParticipant> participantsIt = assignedRole.getParticipantsImmutable().iterator();
        participant = participantsIt.next();

        // For concurrent testing purposes, build another evil participant. He will try to sabotage the claiming
        // process.
        badParticipant = ParticipantFactory.createJannik();

    }

}
